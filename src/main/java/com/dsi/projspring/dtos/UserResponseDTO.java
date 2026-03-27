package com.dsi.projspring.dtos;

import com.dsi.projspring.entities.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    String token;

    private Long activeTicketId;
    private Integer counterNumber;
    private Long counterId;
    private String agencyName;
    private Long agencyId;
}