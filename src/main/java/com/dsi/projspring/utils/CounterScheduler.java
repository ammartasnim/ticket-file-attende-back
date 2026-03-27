package com.dsi.projspring.utils;

import com.dsi.projspring.entities.Counter;
import com.dsi.projspring.repositories.CounterRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CounterScheduler {
    private final CounterRepository counterRepository;
    public CounterScheduler(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Scheduled(cron = "0 * * * * *")
    public void freeStaleCounters() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(2);
        List<Counter> stale = counterRepository.findByAgentIsNotNullAndLastSeenBefore(cutoff);
        for (Counter counter : stale) {
            counter.setAgent(null);
            counter.setActive(false);
            counter.setLastSeen(null);
            System.out.println("Auto-closed stale Counter: " + counter.getId());
        }
        counterRepository.saveAll(stale);
    }
}