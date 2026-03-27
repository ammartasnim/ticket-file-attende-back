package com.dsi.projspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"agency_id", "pin", "appointmentDate"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false)
    private int pin;
    private String name;
    private LocalDateTime timeCreated;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    @Column(nullable = false)
    private LocalDateTime appointmentDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name="service_id",nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name="client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name="counter_id")
    private Counter counter;

    @ManyToOne
    @JoinColumn(name="agency_id", nullable = false)
    private Agency agency;

    @PrePersist
    protected void initialize() {
        if(this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
        timeCreated = LocalDateTime.now();
        this.status = Status.GENERATED;
    }

}
