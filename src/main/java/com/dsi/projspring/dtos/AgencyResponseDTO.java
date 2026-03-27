package com.dsi.projspring.dtos;

import com.dsi.projspring.entities.Counter;
import com.dsi.projspring.entities.Service;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class AgencyResponseDTO {
    private Long id;
    private String name;
    private String city;
    private String address;
    private String openingTime;
    private String closingTime;
    private int maxCapacity;
    private int nbrCounters;
    private boolean isOpen;

    private List<String> serviceNames;
    private int queueSize;
}
