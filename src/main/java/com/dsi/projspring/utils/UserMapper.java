package com.dsi.projspring.utils;

import com.dsi.projspring.dtos.UserResponseDTO;
import com.dsi.projspring.entities.Agent;
import com.dsi.projspring.entities.Client;
import com.dsi.projspring.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toDto(User user) {
        UserResponseDTO.UserResponseDTOBuilder builder = UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole());

        if (user instanceof Agent agent) {
            if (agent.getCounter() != null) {
                builder.counterNumber(agent.getCounter().getNumber());
                builder.counterId(agent.getCounter().getId());
            }
            if (agent.getAgency() != null) {
                builder.agencyName(agent.getAgency().getName());
                builder.agencyId(agent.getAgency().getId());
            }
        }
        if (user instanceof Client client){
            if (client.getActiveTicketId()!=null){
                builder.activeTicketId(client.getActiveTicketId());
            }
        }
        return builder.build();
    }

//    private void mapUserFields(User user, UserResponseDTO dto){
//        dto.setId(user.getId());
//        dto.setFirstName(user.getFirstName());
//        dto.setLastName(user.getLastName());
//        dto.setEmail(user.getEmail());
//        dto.setRole(user.getRole());
//    }
}
