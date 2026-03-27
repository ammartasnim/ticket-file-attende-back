package com.dsi.projspring.services;

import com.dsi.projspring.entities.Agency;
import com.dsi.projspring.entities.Counter;
import com.dsi.projspring.entities.Status;
import com.dsi.projspring.entities.Service;
import com.dsi.projspring.repositories.AgencyRepository;
import com.dsi.projspring.repositories.CounterRepository;
import com.dsi.projspring.repositories.ServiceRepository;
import com.dsi.projspring.repositories.TicketRepository;
import com.dsi.projspring.utils.AgencyMapper;
import com.dsi.projspring.dtos.AgencyRequestDTO;
import com.dsi.projspring.dtos.AgencyResponseDTO;
import jakarta.transaction.Transactional;

import java.time.*;
import java.util.*;

@org.springframework.stereotype.Service
public class AgencyServiceImpl implements IAgencyService {
    private final AgencyRepository agencyRepository;
    private final CounterRepository counterRepository;
    private final ServiceRepository serviceRepository;
    private final TicketRepository ticketRepository;
    private final AgencyMapper agencyMapper;

    public AgencyServiceImpl(AgencyRepository agencyRepository, AgencyMapper agencyMapper,  CounterRepository counterRepository,  TicketRepository ticketRepository, ServiceRepository serviceRepository) {
        this.agencyRepository = agencyRepository;
        this.counterRepository = counterRepository;
        this.serviceRepository = serviceRepository;
        this.ticketRepository = ticketRepository;
        this.agencyMapper = agencyMapper;
    }

    private int getTodayQueueSize(Long agencyId) {
        if (agencyId == null) return 0;
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        Integer count = ticketRepository.countByAgencyIdAndStatusAndAppointmentDateBetween(
                agencyId, Status.GENERATED, start, end);
        return count != null ? count : 0;
    }

    @Override
    public List<AgencyResponseDTO> getAllAgencies() {
        List<Agency>  agencies = agencyRepository.findAll();
        List<AgencyResponseDTO> dtos = new ArrayList<>();
        for (Agency a : agencies) {
            dtos.add(agencyMapper.toDto(a, getTodayQueueSize(a.getId())));
        }
        return dtos;
    }

    @Override
    public List<AgencyResponseDTO> getAgenciesByCity(String city) {
        List<Agency> agencies=agencyRepository.findByCityIgnoreCase(city);
        List<AgencyResponseDTO> dtos = new ArrayList<>();
        for (Agency a : agencies) {
            dtos.add(agencyMapper.toDto(a, getTodayQueueSize(a.getId())));
        }
        return dtos;
    }

    @Override
    public AgencyResponseDTO getAgencyById(Long id) {
        Agency a=agencyRepository.findById(id).orElseThrow(()->new RuntimeException("Agency not found"));
        return agencyMapper.toDto(a, getTodayQueueSize(id));
    }

    @Override
    @Transactional
    public AgencyResponseDTO createAgency(AgencyRequestDTO dto) {
        Agency agency = new Agency();
        agencyMapper.updateEntityFromDto(dto, agency);
        agencyRepository.save(agency);

        List<Service> allServices = serviceRepository.findAll();
        for (Service service : allServices) {
            service.getAgencies().add(agency);
        }
        serviceRepository.saveAll(allServices);

        if (dto.getNbrCounters() > 0) {
            List<Counter> counters = new ArrayList<>();
            for (int i = 1; i <= dto.getNbrCounters(); i++) {
                Counter counter = new Counter();
                counter.setNumber(i);
                counter.setAgency(agency);
                counter.setActive(false);
                counters.add(counter);
            }
            counterRepository.saveAll(counters);
            agency.setCounters(counters);
        }
        return agencyMapper.toDto(agency, getTodayQueueSize(agency.getId()));
    }

    @Override
    @Transactional
    public AgencyResponseDTO updateAgency(Long id, AgencyRequestDTO dto) {
        Agency a=agencyRepository.findById(id).orElseThrow(()->new RuntimeException("Agency not found"));
        agencyMapper.updateEntityFromDto(dto,a);
        agencyRepository.save(a);
        return agencyMapper.toDto(a, getTodayQueueSize(id));
    }

    @Override
    @Transactional
    public void deleteAgency(Long id) {
        Agency a = agencyRepository.findById(id).orElseThrow(() -> new RuntimeException("Agency not found"));
        for (Service service : a.getServices()) {
            service.getAgencies().remove(a);
        }
        serviceRepository.saveAll(a.getServices());
        agencyRepository.delete(a);
    }

    @Override
    @Transactional
    public AgencyResponseDTO toggleIsOpen(Long id){
        Agency a = agencyRepository.findById(id).orElseThrow(() -> new RuntimeException("Agency not found"));
        a.setOpen(!a.isOpen());
        agencyRepository.save(a);
        return agencyMapper.toDto(a, getTodayQueueSize(id));
    }

    @Override
    public AgencyResponseDTO getAgencyByName(String name){
        Agency  a = agencyRepository.findByName(name).orElseThrow(() -> new RuntimeException("Agency not found"));
        return agencyMapper.toDto(a, getTodayQueueSize(a.getId()));
    }

}
