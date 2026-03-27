package com.dsi.projspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Counter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private boolean isActive= false;
    private LocalDateTime lastSeen;

    @ManyToOne
    @JoinColumn(name="agency_id", nullable=false)
    private Agency agency;

    @OneToMany(mappedBy = "counter")
    private List<Ticket> tickets;

    @OneToOne
    @JoinColumn(name = "agent_id", unique = true)
    private User agent;

}
