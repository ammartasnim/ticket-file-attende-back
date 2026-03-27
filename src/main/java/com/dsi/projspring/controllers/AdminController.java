package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.DashboardStatsDTO;
import com.dsi.projspring.dtos.RegisterRequest;
import com.dsi.projspring.dtos.UserResponseDTO;
import com.dsi.projspring.services.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IAuthService authService;

    public AdminController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register_agent")
    public ResponseEntity<String> createAgent(@Valid @RequestBody RegisterRequest request, @RequestParam Long agencyId) {
        authService.registerAgent(request, agencyId);
        return ResponseEntity.ok("Agent created and assigned to agency " + agencyId + " successfully.");
    }

    @GetMapping("/agents/agency/{agencyId}")
    public ResponseEntity<List<UserResponseDTO>> getAgentsByAgency(@PathVariable Long agencyId) {
        return ResponseEntity.ok(authService.getAgentsByAgency(agencyId));
    }

    @GetMapping("/agents")
    public ResponseEntity<List<UserResponseDTO>> getAllAgents() {
        return ResponseEntity.ok(authService.getAllAgents());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(authService.getDashboardStats());
    }
}