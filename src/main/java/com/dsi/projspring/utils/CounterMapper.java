package com.dsi.projspring.utils;

import com.dsi.projspring.dtos.CounterRequestDTO;
import com.dsi.projspring.dtos.CounterResponseDTO;
import com.dsi.projspring.entities.Counter;
import org.springframework.stereotype.Component;

@Component
public class CounterMapper {
    public CounterResponseDTO toDto(Counter counter) {
        if (counter == null) return null;
        CounterResponseDTO dto = new CounterResponseDTO();
        dto.setId(counter.getId());
        dto.setNumber(counter.getNumber());
        dto.setActive(counter.isActive());
        if (counter.getAgency() != null) {
            dto.setAgencyName(counter.getAgency().getName());
        }
        if (counter.getAgent() != null) {
            String fullName = counter.getAgent().getFirstName() + " " + counter.getAgent().getLastName();
            dto.setCurrentAgentName(fullName);
        }
        return dto;
    }

    public void updateEntityFromDto(CounterRequestDTO dto, Counter counter) {
        if (dto == null || counter == null) return;
        counter.setNumber(dto.getNumber());
        counter.setActive(dto.isActive());
    }
}