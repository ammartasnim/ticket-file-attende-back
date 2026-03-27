package com.dsi.projspring.services;

import com.dsi.projspring.dtos.TicketRequestDTO;
import com.dsi.projspring.dtos.TicketResponseDTO;
import com.dsi.projspring.entities.*;
import com.dsi.projspring.repositories.*;
import com.dsi.projspring.utils.TicketMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@org.springframework.stereotype.Service
public class TicketServiceImpl implements ITicketService{

    private final TicketRepository ticketRepository;
    private final CounterRepository counterRepository;
    private final ServiceRepository serviceRepository;
    private final ClientRepository clientRepository;
    private final AgencyRepository agencyRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, CounterRepository counterRepository, TicketMapper ticketMapper, ServiceRepository serviceRepository, ClientRepository clientRepository, AgencyRepository agencyRepository, UserRepository userRepository) {
        this.counterRepository = counterRepository;
        this.ticketRepository = ticketRepository;
        this.serviceRepository = serviceRepository;
        this.clientRepository = clientRepository;
        this.agencyRepository = agencyRepository;
        this.ticketMapper = ticketMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TicketResponseDTO treatTicket(int pin, Long counterId) {
        Optional<Ticket> ticket=ticketRepository.findByCounterIdAndStatus(counterId, Status.CALLED).filter(t->t.getPin()==pin);
        if(ticket.isPresent()){
            Ticket t=ticket.get();
            t.setStatus(Status.TREATING);
            t.setStartedAt(LocalDateTime.now());
            ticketRepository.save(t);
            return ticketMapper.toDto(t);
        }else{
            throw new RuntimeException("Invalid pin for this counter");
        }
    }

    @Override
    @Transactional
    public TicketResponseDTO completeTicket(Long ticketId) {
        Ticket t=ticketRepository.findById(ticketId).orElseThrow(()->new RuntimeException("Ticket not found"));
        t.setStatus(Status.COMPLETED);
        t.setCounter(null);
        t.setFinishedAt(LocalDateTime.now());
        clearClientActiveTicket(t);
        ticketRepository.save(t);
        return ticketMapper.toDto(t);
    }

    @Override
    @Transactional
    public Optional<TicketResponseDTO> callNextTicket(Long counterId) {
        Counter c=counterRepository.findById(counterId).orElseThrow(()->new RuntimeException("Counter not found"));
        LocalDateTime start= LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end= start.toLocalDate().atTime(LocalTime.MAX);
        Optional<Ticket> t=ticketRepository.findFirstByStatusAndAgencyIdAndAppointmentDateBetweenOrderByTimeCreatedAsc(Status.GENERATED,c.getAgency().getId(),start,end);
        if(t.isPresent()){
            Ticket ticket=t.get();
            ticket.setStatus(Status.CALLED);
            ticket.setCounter(c);
            Ticket savedTicket= ticketRepository.save(ticket);
            return Optional.of(ticketMapper.toDto(savedTicket));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public TicketResponseDTO expireTicket(Long ticketId) {
        Ticket t=ticketRepository.findById(ticketId).orElseThrow(()->new RuntimeException("Ticket not found"));
        t.setStatus(Status.EXPIRED);
        t.setCounter(null);
        clearClientActiveTicket(t);
        ticketRepository.save(t);
        return ticketMapper.toDto(t);
    }

    @Override
    @Transactional
    public TicketResponseDTO generateTicket(TicketRequestDTO request) {
        if(request==null)
            return null;
        Service service = serviceRepository.findById(request.getServiceId()).orElseThrow(() -> new EntityNotFoundException("Service not found"));
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        Agency agency = agencyRepository.findById(request.getAgencyId()).orElseThrow(() -> new EntityNotFoundException("Agency not found"));
        List<Status> activeStatuses = Arrays.asList(Status.GENERATED, Status.CALLED, Status.TREATING);
        boolean hasActiveTicket = ticketRepository.existsByClientIdAndStatusIn(client.getId(), activeStatuses);
        if (hasActiveTicket) {
            throw new RuntimeException("You already have an active ticket. Please finish it before booking a new one.");
        }
        Ticket ticket=new Ticket();
        ticketMapper.updateEntityFromDto(request, ticket);
        ticket.setService(service);
        ticket.setClient(client);
        ticket.setAgency(agency);


        LocalDateTime start= ticket.getAppointmentDate().toLocalDate().atStartOfDay();
        LocalDateTime end= ticket.getAppointmentDate().toLocalDate().atTime(LocalTime.MAX);

        Long currentActiveTickets = ticketRepository.countByAgencyIdAndStatusInAndAppointmentDateBetween(
                agency.getId(), activeStatuses, start, end);
        if (currentActiveTickets >= agency.getMaxCapacity()) {
            throw new RuntimeException("This agency has reached its maximum capacity for the selected day. Please try another day or agency.");
        }

        Integer maxTickets=ticketRepository.findMaxPinForAgencyAndDay(agency.getId(),start, end);
        System.out.println("maxTickets: "+maxTickets);
        int pin=1;
        if(maxTickets!=null)
            pin=maxTickets+1;
        ticket.setPin(pin);
        ticket = ticketRepository.save(ticket);
        client.setActiveTicketId(ticket.getId());

        TicketResponseDTO response = ticketMapper.toDto(ticket);
        response.setQueuePosition(this.getQueuePosition(ticket.getId()));
        response.setEstimatedWaitTime(this.calculateWaitTime(ticket.getId()));

        return response;
    }

    @Override
    public int getQueuePosition(Long ticketId) {
        Ticket t=ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        LocalDateTime start= t.getAppointmentDate().toLocalDate().atStartOfDay();
        LocalDateTime end= t.getAppointmentDate().toLocalDate().atTime(LocalTime.MAX);
        int count= ticketRepository.countByStatusAndAgencyIdAndAppointmentDateBetweenAndPinLessThan(Status.GENERATED, t.getAgency().getId(),start,end,t.getPin());
        return count+1;
    }

    @Override
    public int calculateWaitTime(Long ticketId) {
        Ticket t=ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        LocalDateTime start= t.getAppointmentDate().toLocalDate().atStartOfDay();
        LocalDateTime end= t.getAppointmentDate().toLocalDate().atTime(LocalTime.MAX);
        Long agencyId=t.getAgency().getId();
        List<Status> activeStatuses = Arrays.asList(Status.GENERATED, Status.CALLED, Status.TREATING);
        List<Ticket> activeTickets = ticketRepository.findByAgencyIdAndStatusInAndAppointmentDateBetween(agencyId, activeStatuses, start, end);
        int waitTime=0;
        for (Ticket ticket: activeTickets){
            if(ticket.getPin()<t.getPin()){
                waitTime+=ticket.getService().getAvgTime();
            }
        }
        long activeCounters = counterRepository.countByAgencyIdAndIsActiveTrue(agencyId);
        if (activeCounters <= 1) return waitTime;
        return (int) Math.ceil((double) waitTime / activeCounters);
    }

    @Override
    @Transactional
    public TicketResponseDTO cancelTicket(Long ticketId) {
        Ticket t=ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        t.setStatus(Status.CANCELED);
        t.setCounter(null);
        clearClientActiveTicket(t);
        ticketRepository.save(t);
        return ticketMapper.toDto(t);
    }

    @Override
    public List<TicketResponseDTO> getWaitingTickets() {
        List<Ticket> tickets= ticketRepository.findAllByStatus(Status.GENERATED);
        List<TicketResponseDTO> dtos=new ArrayList<>();
        for (Ticket ticket : tickets) {
            dtos.add(ticketMapper.toDto(ticket));
        }
        return dtos;
    }

    @Override
    public List<TicketResponseDTO> getWaitingTicketsByAgency(Long agencyId) {
        LocalDateTime start= LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end= start.toLocalDate().atTime(LocalTime.MAX);
        List<Ticket> tickets=  ticketRepository.findByAgencyIdAndStatusAndAppointmentDateBetween(agencyId, Status.GENERATED, start, end);
        List<TicketResponseDTO> dtos=new ArrayList<>();
        for (Ticket ticket : tickets) {
            dtos.add(ticketMapper.toDto(ticket));
        }
        return dtos;
    }

    @Override
    public Integer countReservationsForDate(Long agencyId, LocalDate date) {
        LocalDateTime start= date.atStartOfDay();
        LocalDateTime end= date.atTime(LocalTime.MAX);
        return ticketRepository.findMaxPinForAgencyAndDay(agencyId, start,end);
    }

    @Override
    public LocalDateTime getEarliestAvailableTime(Long agencyId, Long serviceId, boolean isForTomorrow) {
        LocalDate targetDate = isForTomorrow ? LocalDate.now().plusDays(1) : LocalDate.now();
        LocalDateTime baseTime = isForTomorrow
                ? targetDate.atTime(8, 0) // Assume 08:00 start for tomorrow
                : LocalDateTime.now();

        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        List<Status> activeStatuses = Arrays.asList(Status.GENERATED, Status.CALLED, Status.TREATING);
        List<Ticket> activeTickets = ticketRepository.findByAgencyIdAndStatusInAndAppointmentDateBetween(agencyId, activeStatuses, startOfDay, endOfDay);

        int totalWaitMinutes = 0;
        for (Ticket ticket : activeTickets) {
            totalWaitMinutes += ticket.getService().getAvgTime();
        }

        long activeCounters = counterRepository.countByAgencyIdAndIsActiveTrue(agencyId);

        int waitDuration = (activeCounters <= 1)
                ? totalWaitMinutes
                : (int) Math.ceil((double) totalWaitMinutes / activeCounters);

        return baseTime.plusMinutes(waitDuration);
    }

    @Override
    public TicketResponseDTO getTicketById(Long ticketId) {
        Ticket t=ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        return ticketMapper.toDto(t);
    }

    private void clearClientActiveTicket(Ticket ticket) {
        if (ticket.getClient() != null) {
            Client client = ticket.getClient();
            client.setActiveTicketId(null);
            userRepository.save(client);
        }
    }

    @Override
    @Transactional
    public TicketResponseDTO treatTicketByUuid(String uuid, Long counterId) {
        Ticket ticket = ticketRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("Ticket not found"));

        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Counter not found"));

        if (ticket.getStatus() != Status.GENERATED) {
            throw new RuntimeException("Ticket is not in waiting state");
        }

        ticket.setStatus(Status.TREATING);
        ticket.setCounter(counter);
        ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
    }

    @Override
    @Transactional
    public void expireActiveTicketsForAgency(Long agencyId) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        List<Status> activeStatuses = Arrays.asList(Status.GENERATED, Status.CALLED, Status.TREATING);

        // 2. Find all tickets that are still "stuck" in the system for this agency today
        List<Ticket> stuckTickets = ticketRepository.findByAgencyIdAndStatusInAndAppointmentDateBetween(
                agencyId, activeStatuses, start, end);

        for (Ticket t : stuckTickets) {
            t.setStatus(Status.EXPIRED);
            t.setCounter(null);
            clearClientActiveTicket(t);
            ticketRepository.save(t);
        }
        System.out.println("Expired " + stuckTickets.size() + " tickets for Agency ID: " + agencyId);
    }

    @Override
    public List<TicketResponseDTO> getTicketsByClientId(Long clientId){
        List<Ticket> tickets=ticketRepository.findByClientId(clientId);
        List<TicketResponseDTO> dtos=new ArrayList<>();
        for (Ticket ticket : tickets) {
            dtos.add(ticketMapper.toDto(ticket));
        }
        return dtos;
    }




}
