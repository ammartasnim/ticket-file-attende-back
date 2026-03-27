package com.dsi.projspring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("AGENT")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Agent extends User{
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @OneToOne(mappedBy = "agent")
    private Counter counter;

    public Agent(Long id, String firstName, String lastName, String email, String password, String phoneNumber, Role role) {
        super(id, firstName, lastName, email, password, phoneNumber, role, true);
    }
}
