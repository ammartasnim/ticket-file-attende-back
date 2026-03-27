package com.dsi.projspring.config;

import com.dsi.projspring.entities.Admin;
import com.dsi.projspring.entities.Agency;
import com.dsi.projspring.entities.Agent;
import com.dsi.projspring.entities.Role;
import com.dsi.projspring.repositories.AgencyRepository;
import com.dsi.projspring.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, AgencyRepository agencyRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            // 3. Initialize the Admin
            if (userRepository.findByEmail("admin@test.com").isEmpty()) {
                Admin testAdmin = new Admin();
                testAdmin.setFirstName("admin");
                testAdmin.setLastName("admin");
                testAdmin.setEmail("admin@test.com");
                testAdmin.setPassword(passwordEncoder.encode("admin"));
                testAdmin.setRole(Role.ADMIN);
                testAdmin.setActive(true);

                userRepository.save(testAdmin);
                System.out.println("✅ Test Admin created: admin@test.com");
            }
        };
    }
}