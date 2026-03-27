package com.dsi.projspring.services;

import com.dsi.projspring.dtos.ServiceRequestDTO;
import com.dsi.projspring.dtos.ServiceResponseDTO;

import java.util.List;

public interface IServiceService {
    List<ServiceResponseDTO> getAllServices();
    List<ServiceResponseDTO> getServicesByAgency(Long agencyId);
    ServiceResponseDTO getServiceById(Long serviceId);
    ServiceResponseDTO createService(ServiceRequestDTO dto);
    void deleteService(Long id);
}
