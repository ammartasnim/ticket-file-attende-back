package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.CounterResponseDTO;
import com.dsi.projspring.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentRestController {

    private final ICounterService counterService;

    public AgentRestController(ICounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping("/open/{counterId}")
    public ResponseEntity<CounterResponseDTO> openCounter(@PathVariable Long counterId,@RequestParam Long agentId) {
        return ResponseEntity.ok(counterService.loginToCounter(counterId, agentId));
    }

    @PostMapping("/close/{counterId}")
    public ResponseEntity<CounterResponseDTO> closeCounter(@PathVariable Long counterId) {
        return ResponseEntity.ok(counterService.logoutFromCounter(counterId));
    }

    @PostMapping("/heartbeat/{counterId}")
    public ResponseEntity<Void> heartbeat(@PathVariable Long counterId) {
        counterService.updateHeartbeat(counterId);
        return ResponseEntity.ok().build();
    }
}