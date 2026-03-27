package com.dsi.projspring.dtos;

import lombok.Data;

@Data
public class AgencyRequestDTO {
    private String name;
    private String city;
    private String address;
    private String openingTime;
    private String closingTime;
    private int maxCapacity;
    private int nbrCounters;
    private boolean isOpen;
}
