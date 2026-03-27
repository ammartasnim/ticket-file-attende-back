package com.dsi.projspring.services;


import com.dsi.projspring.dtos.AgencyResponseDTO;
import com.dsi.projspring.dtos.AgencyRequestDTO;

import java.util.List;

public interface IAgencyService {
    List<AgencyResponseDTO> getAllAgencies();
    List<AgencyResponseDTO> getAgenciesByCity(String city);
    AgencyResponseDTO getAgencyById(Long id);
    AgencyResponseDTO toggleIsOpen(Long id);
    AgencyResponseDTO createAgency(AgencyRequestDTO dto);
    AgencyResponseDTO updateAgency(Long id, AgencyRequestDTO dto);
    void deleteAgency(Long id);
    AgencyResponseDTO getAgencyByName(String name);
}
