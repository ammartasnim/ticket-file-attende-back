package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.ServiceRequestDTO;
import com.dsi.projspring.dtos.ServiceResponseDTO;
import com.dsi.projspring.services.IServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {
    private final IServiceService serviceService;
    public ServiceController(IServiceService serviceService) {
        this.serviceService = serviceService;
    }
    @GetMapping
    public List<ServiceResponseDTO> getAllServices() {
        return serviceService.getAllServices();
    }

    @GetMapping("/agency/{agencyId}")
    public List<ServiceResponseDTO> getServiceByAgencyId(@PathVariable Long agencyId) {
        return serviceService.getServicesByAgency(agencyId);
    }

    @GetMapping("/{id}")
    public ServiceResponseDTO getServiceById(@PathVariable Long serviceId) {
        return serviceService.getServiceById(serviceId);
    }

    @PostMapping
    public ResponseEntity<ServiceResponseDTO> createService(@RequestBody ServiceRequestDTO dto) {
        return ResponseEntity.ok().body(serviceService.createService(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}

