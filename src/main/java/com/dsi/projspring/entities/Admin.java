package com.dsi.projspring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Admin extends User{
    public Admin(Long id, String firstName, String lastName, String email, String password, String phoneNumber, Role role) {
        super(id, firstName, lastName, email, password, phoneNumber, role, true);
    }
}
