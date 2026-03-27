package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.CounterRequestDTO;
import com.dsi.projspring.dtos.CounterResponseDTO;
import com.dsi.projspring.services.ICounterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counters")
public class CounterController {
    private final ICounterService counterService;
    public CounterController(ICounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounterResponseDTO> getCounter(@PathVariable Long id) {
        return ResponseEntity.ok(counterService.getCounterById(id));
    }

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<List<CounterResponseDTO>> getCountersByAgency(@PathVariable Long agencyId) {
        return ResponseEntity.ok(counterService.getCountersByAgency(agencyId));
    }

    @PostMapping
    public ResponseEntity<CounterResponseDTO> createCounter(@Valid @RequestBody CounterRequestDTO dto) {
        return ResponseEntity.ok(counterService.createCounter(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CounterResponseDTO> updateCounter(@PathVariable Long id, @RequestBody CounterRequestDTO dto) {
        return ResponseEntity.ok(counterService.updateCounter(id, dto));
    }

    @PatchMapping("/toggle/{id}")
    public ResponseEntity<CounterResponseDTO> toggleCounter(@PathVariable Long id) {
        return ResponseEntity.ok(counterService.toggleStatus(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounter(@PathVariable Long id) {
        counterService.deleteCounter(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/agency/{agencyId}/shutdown-counters")
    public ResponseEntity<Void> shutdownCounters(@PathVariable Long agencyId) {
        counterService.deactivateAllCountersForAgency(agencyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("heartbeat/{id}")
    public ResponseEntity<Void> heartbeat(@PathVariable Long id) {
        counterService.updateHeartbeat(id);
        return ResponseEntity.ok().build();
    }


}