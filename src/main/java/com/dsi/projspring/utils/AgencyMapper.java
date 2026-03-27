package com.dsi.projspring.utils;

import com.dsi.projspring.dtos.AgencyRequestDTO;
import com.dsi.projspring.dtos.AgencyResponseDTO;
import com.dsi.projspring.entities.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class AgencyMapper {
    public AgencyResponseDTO toDto(Agency agency, int queueSize){
        if(agency == null){ return null; }
        AgencyResponseDTO dto = new AgencyResponseDTO();
        dto.setId(agency.getId());
        dto.setName(agency.getName());
        dto.setAddress(agency.getAddress());
        dto.setCity(agency.getCity());
        dto.setOpen(agency.isOpen());
        dto.setOpeningTime(agency.getOpeningTime());
        dto.setClosingTime(agency.getClosingTime());
        dto.setMaxCapacity(agency.getMaxCapacity());
        dto.setNbrCounters(agency.getNbrCounters());
        dto.setQueueSize(queueSize);

        List<String> names = new ArrayList<>();
        if (agency.getServices() != null) {
            for(Service s : agency.getServices()){
                if(s != null){
                    names.add(s.getName());
                }
            }
        }
        dto.setServiceNames(names);
        return dto;
    }

    public void updateEntityFromDto(AgencyRequestDTO dto, Agency agency) {
        if (dto == null) return;
        agency.setName(dto.getName());
        agency.setCity(dto.getCity());
        agency.setAddress(dto.getAddress());
        agency.setOpeningTime(dto.getOpeningTime());
        agency.setClosingTime(dto.getClosingTime());
        agency.setMaxCapacity(dto.getMaxCapacity());
        agency.setNbrCounters(dto.getNbrCounters());
        agency.setOpen(dto.isOpen());
    }
}
