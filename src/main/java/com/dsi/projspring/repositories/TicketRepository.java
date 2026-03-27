package com.dsi.projspring.repositories;

import com.dsi.projspring.entities.Agency;
import com.dsi.projspring.entities.Status;
import com.dsi.projspring.entities.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.time.*;
import java.util.*;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByClientId(Long clientId);

    Optional<Ticket> findFirstByStatusAndAgencyIdAndAppointmentDateBetweenOrderByTimeCreatedAsc(Status status, Long agencyId, LocalDateTime start, LocalDateTime end);

    Optional<Ticket> findByCounterIdAndStatus(Long counterId, Status status);

    boolean existsByClientIdAndStatusIn(Long clientId, List<Status> statuses);

////    @Query("Select t from Ticket t join t.service.agencies a where a.id= :agencyId and t.status='GENERATED'")
////    List<Ticket> findWaitingTicketsByAgency(@Param("agencyId") Long agencyId);
//    List<Ticket> findAllByServiceAgenciesIdAndStatusGENERATED(Long agencyId);
    List<Ticket> findAllByStatus(Status status);

    List<Ticket> findByAgencyIdAndStatusInAndAppointmentDateBetween(Long agencyId, Collection<Status> status,  LocalDateTime start, LocalDateTime end);
    List<Ticket> findByAgencyIdAndStatusAndAppointmentDateBetween(Long agencyId, Status status,  LocalDateTime start, LocalDateTime end);


    //    @Query("select max(t.pin) from Ticket t where t.timeCreated>= :startOfDay")
//    Integer findMaxPinForDay(@Param("startOfDay") LocalDateTime startOfDay);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(t.pin) FROM Ticket t WHERE t.agency.id=:agencyId and t.appointmentDate>= :start AND t.appointmentDate<= :end")
    Integer findMaxPinForAgencyAndDay(@Param("agencyId") Long agencyId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //Long countByStatusGENERATEDAndTimeCreatedBefore(LocalDateTime timeCreated);
    Integer countByStatusAndAgencyIdAndAppointmentDateBetweenAndPinLessThan(Status status, Long agencyId, LocalDateTime start, LocalDateTime end, int pin);

    // Total people waiting in an agency today
    Integer countByAgencyIdAndStatusAndAppointmentDateBetween(Long agencyId, Status status, LocalDateTime start, LocalDateTime end);
    long countByAgencyIdAndStatusInAndAppointmentDateBetween(
            Long agencyId,
            List<Status> statuses,
            LocalDateTime start,
            LocalDateTime end
    );
    boolean existsByClientIdAndStatusIn(Long clientId, Collection<Status> statuses);
    Optional<Ticket> findByUuid(String uuid);

    Long agency(Agency agency);

    Long countByAppointmentDateBetween(LocalDateTime start,  LocalDateTime end);
    Long countByStatus(Status status);
}
