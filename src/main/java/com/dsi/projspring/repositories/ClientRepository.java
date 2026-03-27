package com.dsi.projspring.repositories;

import com.dsi.projspring.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
