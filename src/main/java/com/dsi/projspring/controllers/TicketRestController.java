package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.TicketRequestDTO;
import com.dsi.projspring.dtos.TicketResponseDTO;
import com.dsi.projspring.entities.Ticket;
import com.dsi.projspring.services.ITicketService;
import com.dsi.projspring.utils.TicketMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/tickets")
//@CrossOrigin(origins = "*")
public class TicketRestController {
    private final ITicketService ticketService;
    public TicketRestController(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/generate")
    public ResponseEntity<TicketResponseDTO> generateTicket(@Valid @RequestBody TicketRequestDTO ticketRequestDTO) {
        return ResponseEntity.ok(ticketService.generateTicket(ticketRequestDTO));
    }

    @GetMapping()
    public ResponseEntity<List<TicketResponseDTO>> getTickets() {
        return ResponseEntity.ok(ticketService.getWaitingTickets());
    }

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByAgency(@PathVariable Long agencyId) {
        return ResponseEntity.ok(ticketService.getWaitingTicketsByAgency(agencyId));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/expire/{id}")
    public ResponseEntity<TicketResponseDTO> expireTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.expireTicket(id));
    }

    @PutMapping("/treat/{id}")
    public ResponseEntity<TicketResponseDTO> treatTicket(@PathVariable Long id, @RequestParam int pin) {
        return ResponseEntity.ok(ticketService.treatTicket(pin, id));
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<TicketResponseDTO> completeTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.completeTicket(id));
    }

    @PostMapping("/next/{counterId}")
    public ResponseEntity<TicketResponseDTO> callNext(@PathVariable Long counterId) {
        Optional<TicketResponseDTO> ticket = ticketService.callNextTicket(counterId);
        if (ticket.isPresent()) {
            return ResponseEntity.ok(ticket.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/position/{id}")
    public ResponseEntity<Integer> getPosition(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getQueuePosition(id));
    }

    @GetMapping("/waitTime/{id}")
    public ResponseEntity<Integer> getWaitTime(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.calculateWaitTime(id));
    }

    @GetMapping("/earliest")
    public ResponseEntity<LocalDateTime> getEarliestTime(
            @RequestParam Long agencyId,
            @RequestParam Long serviceId,
            @RequestParam(defaultValue = "false") boolean tomorrow) {

        LocalDateTime earliest = ticketService.getEarliestAvailableTime(agencyId, serviceId, tomorrow);
        return ResponseEntity.ok(earliest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PutMapping("/treat-by-uuid/{uuid}")
    public ResponseEntity<TicketResponseDTO> treatByUuid(
            @PathVariable String uuid,
            @RequestParam Long counterId) {
        return ResponseEntity.ok(ticketService.treatTicketByUuid(uuid, counterId));
    }

    @PutMapping("/agency/{agencyId}/force-expire-all")
    public ResponseEntity<Void> forceExpireTickets(@PathVariable Long agencyId) {
        ticketService.expireActiveTicketsForAgency(agencyId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByClientId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketsByClientId(id));
    }


}
