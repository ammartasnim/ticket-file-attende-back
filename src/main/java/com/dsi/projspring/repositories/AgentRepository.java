package com.dsi.projspring.repositories;

import com.dsi.projspring.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByCounterId(Long counterId);
    List<Agent> findByAgency(Long agencyId);
}
