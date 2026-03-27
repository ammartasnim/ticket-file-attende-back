package com.dsi.projspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Client extends User{
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
    private Long activeTicketId;

    public Client(Long id, String firstName, String lastName, String email, String password, String phoneNumber, Role role) {
        super(id, firstName, lastName, email, password, phoneNumber, role, true);
    }
}
