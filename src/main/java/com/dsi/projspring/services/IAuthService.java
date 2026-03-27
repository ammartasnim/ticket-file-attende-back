package com.dsi.projspring.services;

import com.dsi.projspring.dtos.*;

import java.util.List;

public interface IAuthService {
    UserResponseDTO login(LoginRequest request);
    void register(RegisterRequest request);
    void registerAgent(RegisterRequest request, Long agencyId);
    void deleteUser(Long id);

    List<UserResponseDTO> getAgentsByAgency(Long agencyId);
    List<UserResponseDTO> getAllAgents();

    void changePassword(Long userId, ChangePasswordRequest request);
    UserResponseDTO getMe(String email);

    DashboardStatsDTO getDashboardStats();
}
