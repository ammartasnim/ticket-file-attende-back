package com.dsi.projspring.utils;

import com.dsi.projspring.dtos.TicketRequestDTO;
import com.dsi.projspring.dtos.TicketResponseDTO;
import com.dsi.projspring.entities.*;
import com.dsi.projspring.repositories.*;
import org.springframework.stereotype.Component;


@Component
public class TicketMapper {
//    private final ServiceRepository serviceRepository;
//    private final ClientRepository clientRepository;
//    private final AgencyRepository agencyRepository;
//    public TicketMapper(ServiceRepository serviceRepository, ClientRepository clientRepository, AgencyRepository agencyRepository) {
//        this.serviceRepository = serviceRepository;
//        this.clientRepository = clientRepository;
//        this.agencyRepository = agencyRepository;
//    }

//    public TicketRequestDTO toDTO(Ticket ticket) {
//        return new TicketRequestDTO(ticket.getService().getId(),ticket.getClient().getId(),ticket.getAgency().getId(),ticket.getAppointmentDate());
//    }
//    public Ticket toEntity(TicketRequestDTO dto) {
//        if(dto==null)
//            return null;
//        Ticket ticket = new Ticket();
//        ticket.setAppointmentDate(dto.getAppointmentDate());
//        ticket.setService(serviceRepository.findById(dto.getServiceId()).orElse(null));
//        ticket.setClient(clientRepository.findById(dto.getClientId()).orElse(null));
//        ticket.setAgency(agencyRepository.findById(dto.getAgencyId()).orElse(null));
//        return ticket;
//    }
    public void updateEntityFromDto(TicketRequestDTO dto, Ticket ticket) {
        if (dto == null || ticket==null) return;
        ticket.setAppointmentDate(dto.getAppointmentDate());
    }
    public TicketResponseDTO toDto(Ticket ticket) {
        if(ticket==null)
            return null;
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setUuid(ticket.getUuid());
        dto.setPin(ticket.getPin());
        dto.setStatus(ticket.getStatus());
        dto.setAppointmentDate(ticket.getAppointmentDate());

        if (ticket.getService() != null)
            dto.setServiceName(ticket.getService().getName());
        if (ticket.getAgency() != null) {
            dto.setAgencyName(ticket.getAgency().getName());
            dto.setAgencyAddress(ticket.getAgency().getAddress());
        }
        if(ticket.getClient() != null)
            dto.setClientName(ticket.getClient().getFirstName()+" "+ticket.getClient().getLastName());
        if(ticket.getCounter()!=null)
            dto.setCounterNumber(ticket.getCounter().getNumber());

        return dto;
    }
}