package com.dsi.projspring.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestDTO {
    private String name;
    private int avgTime;
    private String description;
}
