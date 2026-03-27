package com.dsi.projspring.dtos;

import lombok.Data;

@Data
public class CounterResponseDTO {
    private Long id;
    private int number;
    private boolean isActive;
    private String agencyName;
    private String currentAgentName;
}