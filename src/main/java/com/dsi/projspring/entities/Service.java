package com.dsi.projspring.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private int avgTime;
    private String description;

    @ManyToMany
    @JoinTable(name="service_agency",
            joinColumns= @JoinColumn(name="service_id"),
            inverseJoinColumns = @JoinColumn(name="agency_id")
    )
    private List<Agency> agencies;

    @OneToMany(mappedBy = "service")
    private List<Ticket> tickets;
}
