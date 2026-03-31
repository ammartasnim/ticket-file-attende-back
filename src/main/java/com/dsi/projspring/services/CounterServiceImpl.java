package com.dsi.projspring.services;

import com.dsi.projspring.dtos.CounterRequestDTO;
import com.dsi.projspring.dtos.CounterResponseDTO;
import com.dsi.projspring.entities.Agency;
import com.dsi.projspring.entities.Counter;
import com.dsi.projspring.entities.User;
import com.dsi.projspring.repositories.CounterRepository;
import com.dsi.projspring.repositories.AgencyRepository;
import com.dsi.projspring.repositories.UserRepository;
import com.dsi.projspring.utils.CounterMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CounterServiceImpl implements ICounterService {
    private final CounterRepository counterRepository;
    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;
    private final CounterMapper counterMapper;
    public CounterServiceImpl(CounterRepository counterRepository, AgencyRepository agencyRepository, CounterMapper counterMapper, UserRepository userRepository) {
        this.counterRepository = counterRepository;
        this.agencyRepository = agencyRepository;
        this.userRepository = userRepository;
        this.counterMapper = counterMapper;
    }

    @Override
    public List<CounterResponseDTO> getCountersByAgency(Long agencyId) {
        List<Counter> counters=counterRepository.findByAgencyId(agencyId);
        List<CounterResponseDTO> dtos=new ArrayList<>();
        for(Counter c:counters){
            dtos.add(counterMapper.toDto(c));
        }
        return dtos;
    }

    @Override
    public CounterResponseDTO getCounterById(Long id) {
        Counter counter = counterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Counter not found"));
        return counterMapper.toDto(counter);
    }

    @Override
    @Transactional
    public CounterResponseDTO createCounter(CounterRequestDTO dto) {
        Agency agency = agencyRepository.findById(dto.getAgencyId()).orElseThrow(() -> new EntityNotFoundException("Agency not found"));
        Counter c=new Counter();
        counterMapper.updateEntityFromDto(dto,c);
        c.setAgency(agency);
        counterRepository.save(c);
        return counterMapper.toDto(c);
    }

    @Override
    @Transactional
    public CounterResponseDTO updateCounter(Long id, CounterRequestDTO dto) {
        Counter counter=counterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Counter not found"));
        counterMapper.updateEntityFromDto(dto,counter);
        counterRepository.save(counter);
        return counterMapper.toDto(counter);
    }

    @Override
    public void deleteCounter(Long id) {
        Counter c=counterRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Counter not found"));
        counterRepository.delete(c);
    }

    @Override
    @Transactional
    public CounterResponseDTO toggleStatus(Long id) {
        Counter counter= counterRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Counter not found"));
        counter.setActive(!counter.isActive());
        counterRepository.save(counter);
        return counterMapper.toDto(counter);
    }

    @Override
    @Transactional
    public CounterResponseDTO loginToCounter(Long id, Long agentId) {
        Counter counter = counterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Counter not found"));
        User agent = userRepository.findById(agentId).orElseThrow(() -> new EntityNotFoundException("Agent not found"));

        if (counter.getAgent() != null && counter.isActive() && !counter.getAgent().equals(agent)) {
            throw new RuntimeException("This counter is already occupied by another agent");
        }
        counter.setAgent(agent);
        counter.setActive(true);
        counter.setLastSeen(LocalDateTime.now());
        counterRepository.save(counter);
        return counterMapper.toDto(counter);
    }

    @Override
    @Transactional
    public CounterResponseDTO logoutFromCounter(Long counterId) {
        Counter counter = counterRepository.findById(counterId).orElseThrow(() -> new EntityNotFoundException("Counter not found"));
        counter.setAgent(null);
        counter.setActive(false);
        counter.setLastSeen(null);
        counterRepository.save(counter);
        return counterMapper.toDto(counter);
    }

    @Override
    public Long countActiveCounters(Long agencyId) {
        return counterRepository.countByAgencyIdAndIsActiveTrue(agencyId);
    }

    @Override
    @Transactional
    public void updateHeartbeat(Long counterId) {
        Counter counter = counterRepository.findById(counterId).orElseThrow(() -> new RuntimeException("Counter not found"));
        counter.setLastSeen(LocalDateTime.now());
        counterRepository.save(counter);
    }

    @Override
    @Transactional
    public void deactivateAllCountersForAgency(Long agencyId) {
        List<Counter> activeCounters = counterRepository.findByAgencyIdAndIsActiveTrue(agencyId);

        for (Counter counter : activeCounters) {
            counter.setActive(false);
            counterRepository.save(counter);
        }
        System.out.println("Deactivated " + activeCounters.size() + " counters for Agency: " + agencyId);
    }
}
