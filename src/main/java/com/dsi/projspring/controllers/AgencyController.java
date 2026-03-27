package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.AgencyRequestDTO;
import com.dsi.projspring.dtos.AgencyResponseDTO;
import com.dsi.projspring.services.IAgencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agencies")
public class AgencyController {
    private final IAgencyService agencyService;
    public AgencyController(IAgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @GetMapping
    public List<AgencyResponseDTO> getAllAgencies() {
        return agencyService.getAllAgencies();
    }

    @GetMapping("/city/{city}")
    public List<AgencyResponseDTO> getAgenciesByCity(@PathVariable String city) {
        return agencyService.getAgenciesByCity(city);
    }

    @GetMapping("/name/{name}")
    public AgencyResponseDTO getAgencyByName(@PathVariable String name) {
        return agencyService.getAgencyByName(name);
    }

    @GetMapping("/{id}")
    public AgencyResponseDTO getAgencyById(@PathVariable Long id) {
        return agencyService.getAgencyById(id);
    }

    @PostMapping
    public ResponseEntity<AgencyResponseDTO> createAgency(@RequestBody AgencyRequestDTO dto) {
        return ResponseEntity.ok().body(agencyService.createAgency(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgencyResponseDTO> updateAgency(@PathVariable Long id, @RequestBody AgencyRequestDTO dto) {
        return ResponseEntity.ok(agencyService.updateAgency(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable Long id) {
        agencyService.deleteAgency(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("toggle/{id}")
    public ResponseEntity<AgencyResponseDTO> toggleIsOpen(@PathVariable Long id) {
        return ResponseEntity.ok(agencyService.toggleIsOpen(id));
    }
}
