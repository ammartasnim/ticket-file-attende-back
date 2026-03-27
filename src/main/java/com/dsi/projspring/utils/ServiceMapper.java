package com.dsi.projspring.utils;

import com.dsi.projspring.dtos.ServiceRequestDTO;
import com.dsi.projspring.dtos.ServiceResponseDTO;
import com.dsi.projspring.entities.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {
    public ServiceResponseDTO toDto(Service service){
        if(service==null){return null;}
        ServiceResponseDTO dto=new ServiceResponseDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setAvgTime(service.getAvgTime());
        return dto;
    }
    public void updateEntityFromDto(ServiceRequestDTO dto, Service service){
        if(dto==null){return;}
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setAvgTime(dto.getAvgTime());
    }
}
