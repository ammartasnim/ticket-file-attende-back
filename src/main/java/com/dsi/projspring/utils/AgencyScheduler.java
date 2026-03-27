package com.dsi.projspring.utils;

import com.dsi.projspring.entities.Agency;
import com.dsi.projspring.repositories.AgencyRepository;
import com.dsi.projspring.services.CounterServiceImpl;
import com.dsi.projspring.services.ICounterService;
import com.dsi.projspring.services.ITicketService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class AgencyScheduler {
    private final AgencyRepository agencyRepository;
    private final ITicketService ticketService;
    private final ICounterService counterService;

    public AgencyScheduler(AgencyRepository agencyRepository, ITicketService ticketService, ICounterService counterService) {
        this.agencyRepository = agencyRepository;
        this.ticketService = ticketService;
        this.counterService = counterService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void updateAgencyOpenStatus() {
        LocalTime now = LocalTime.now(ZoneId.of("Africa/Tunis"));
        List<Agency> agencies = agencyRepository.findAll();

        for (Agency agency : agencies) {
            if (agency.getOpeningTime() == null || agency.getClosingTime() == null) continue;
            try {
                LocalTime opening = LocalTime.parse(agency.getOpeningTime());
                LocalTime closing = LocalTime.parse(agency.getClosingTime());

                boolean shouldBeOpen = !now.isBefore(opening) && now.isBefore(closing);

                if (agency.isOpen() && !shouldBeOpen) {
                    System.out.println("Agency " + agency.getName() + " is closing. Expiring active tickets & closing active counters.");
                    ticketService.expireActiveTicketsForAgency(agency.getId());
                    counterService.deactivateAllCountersForAgency(agency.getId());
                }
                if (agency.isOpen() != shouldBeOpen) {
                    agency.setOpen(shouldBeOpen);
                    agencyRepository.save(agency);
                }
            } catch (Exception e) {
                System.err.println("Error parsing time for agency " + agency.getName() + ": " + e.getMessage());
            }
        }
    }
}
