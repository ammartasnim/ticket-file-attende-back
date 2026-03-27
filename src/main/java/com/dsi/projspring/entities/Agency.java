package com.dsi.projspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agency{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String city;
    private String address;
    private String openingTime;
    private String closingTime;
    private int maxCapacity;
    private int nbrCounters;
    private boolean isOpen;

    @OneToMany(mappedBy="agency", cascade=CascadeType.ALL)
    private List<Counter> counters;

    @ManyToMany(mappedBy = "agencies")
    private List<Service> services= new ArrayList<>();

    @OneToMany(mappedBy="agency")
    private List<Ticket> tickets= new ArrayList<>();

    @OneToMany(mappedBy = "agency")
    private List<Agent> agents= new ArrayList<>();
}
