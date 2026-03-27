package com.dsi.projspring.services;

import com.dsi.projspring.dtos.CounterRequestDTO;
import com.dsi.projspring.dtos.CounterResponseDTO;

import java.util.List;

public interface ICounterService {
    List<CounterResponseDTO> getCountersByAgency(Long agencyId);
    CounterResponseDTO getCounterById(Long id);
    CounterResponseDTO createCounter(CounterRequestDTO dto);
    CounterResponseDTO updateCounter(Long id, CounterRequestDTO dto);
    void deleteCounter(Long id);
    CounterResponseDTO toggleStatus(Long id);
    CounterResponseDTO loginToCounter(Long id, Long agentId);
    CounterResponseDTO logoutFromCounter(Long id);
    Long countActiveCounters(Long agencyId);
    void deactivateAllCountersForAgency(Long agencyId);

    void updateHeartbeat(Long counterId);
}
