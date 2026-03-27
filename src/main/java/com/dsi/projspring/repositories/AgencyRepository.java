package com.dsi.projspring.repositories;

import com.dsi.projspring.entities.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Optional<Agency> findByName(String name);
    List<Agency> findByCityIgnoreCase(String city);

}
