package com.dsi.projspring.repositories;

import com.dsi.projspring.entities.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CounterRepository extends JpaRepository<Counter, Long> {
    List<Counter> findByAgencyId(Long agencyId);

    Long countByAgencyIdAndIsActiveTrue(Long agencyId);

    List<Counter> findByAgentIsNotNullAndLastSeenBefore(LocalDateTime cutoff);
    List<Counter> findByAgencyIdAndIsActiveTrue(Long agencyId);

    Long countByIsActiveTrue();
}
