package com.dsi.projspring;

import com.dsi.projspring.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjspringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjspringApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            System.out.println("--- CURRENT USERS IN DB ---");
            repo.findAll().forEach(user -> System.out.println("Email: " + user.getEmail()));
            System.out.println("---------------------------");
        };
    }
}
