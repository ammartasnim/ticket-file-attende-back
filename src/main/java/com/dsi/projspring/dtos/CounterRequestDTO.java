package com.dsi.projspring.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CounterRequestDTO {
    private int number;
    @NotNull
    private Long agencyId;
    private boolean isActive;
}