package com.dsi.projspring.services;

import com.dsi.projspring.dtos.TicketRequestDTO;
import com.dsi.projspring.dtos.TicketResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface ITicketService {
    TicketResponseDTO treatTicket(int pin, Long counterId);
    TicketResponseDTO completeTicket(Long ticketId);
    Optional<TicketResponseDTO> callNextTicket(Long counterId);
    List<TicketResponseDTO> getTicketsByClientId(Long clientId);

    TicketResponseDTO expireTicket(Long ticketId);

    TicketResponseDTO generateTicket(TicketRequestDTO request);
    int getQueuePosition(Long ticketId);
    int calculateWaitTime(Long ticketId);
    TicketResponseDTO cancelTicket(Long ticketId);

    List<TicketResponseDTO> getWaitingTickets();
    List<TicketResponseDTO> getWaitingTicketsByAgency(Long agencyId);
    Integer countReservationsForDate(Long agencyId, LocalDate date);
    LocalDateTime getEarliestAvailableTime(Long agencyId, Long serviceId, boolean isForTomorrow);
    TicketResponseDTO getTicketById(Long ticketId);
    TicketResponseDTO treatTicketByUuid(String uuid, Long counterId);
    void expireActiveTicketsForAgency(Long agencyId);
}
