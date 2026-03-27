package com.dsi.projspring.repositories;

import com.dsi.projspring.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service,Long> {
    List<Service> findAllByAgenciesId(Long agenciesId);
    List<Service> findAll();


}
