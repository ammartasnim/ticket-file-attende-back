package com.dsi.projspring.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDTO {
    private Long id;
    private String name;
    private int avgTime;
    private String description;
}
