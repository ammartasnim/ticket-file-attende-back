package com.dsi.projspring.dtos;

import com.dsi.projspring.entities.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class TicketResponseDTO {
    private Long id;
    private String uuid;
    private int pin;
    private Status status;
    private LocalDateTime appointmentDate;

    private String serviceName;
    private String agencyName;
    private String agencyAddress;
    private String clientName;
    private Integer counterNumber;

    private int queuePosition;
    private int estimatedWaitTime;
}
