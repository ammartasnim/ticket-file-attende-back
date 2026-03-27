package com.dsi.projspring.services;

import com.dsi.projspring.dtos.ServiceRequestDTO;
import com.dsi.projspring.dtos.ServiceResponseDTO;
import com.dsi.projspring.entities.Agency;
import com.dsi.projspring.entities.Service;
import com.dsi.projspring.repositories.AgencyRepository;
import com.dsi.projspring.repositories.ServiceRepository;
import com.dsi.projspring.utils.ServiceMapper;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements IServiceService{
    private final ServiceRepository serviceRepository;
    private final AgencyRepository agencyRepository;
    private final ServiceMapper serviceMapper;
    public ServiceServiceImpl(ServiceRepository serviceRepository, ServiceMapper serviceMapper,  AgencyRepository agencyRepository) {
        this.serviceRepository = serviceRepository;
        this.agencyRepository = agencyRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public List<ServiceResponseDTO> getAllServices() {
        List<Service> services=serviceRepository.findAll();
        List<ServiceResponseDTO> dtos=new ArrayList<>();
        for(Service s:services){
            dtos.add(serviceMapper.toDto(s));
        }
        return dtos;
    }

    @Override
    public ServiceResponseDTO getServiceById(Long serviceId) {
        Service service=serviceRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        return serviceMapper.toDto(service);
    }

    @Override
    public List<ServiceResponseDTO> getServicesByAgency(Long agencyId) {
        List<Service> services=serviceRepository.findAllByAgenciesId(agencyId);
        List<ServiceResponseDTO> dtos=new ArrayList<>();
        for(Service s:services){
            dtos.add(serviceMapper.toDto(s));
        }
        return dtos;
    }

    @Override
    @Transactional
    public ServiceResponseDTO createService(ServiceRequestDTO dto) {
        Service s=new Service();
        serviceMapper.updateEntityFromDto(dto, s);
        List<Agency> allAgencies = agencyRepository.findAll();
        s.setAgencies(allAgencies);
        serviceRepository.save(s);
        return serviceMapper.toDto(s);
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        Service s= serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        serviceRepository.delete(s);
    }
}
