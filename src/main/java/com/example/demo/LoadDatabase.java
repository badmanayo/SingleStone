package com.example.demo;

import com.example.demo.Account.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

// The purpose of this class is to load the database
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(AccountRepo repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Account("Harold", "Francis", "Gilkey","8360 High Autumn Row", "Cannon", "Delaware","19797", "harold.gilkey@yahoo.com", new Phone("302-611-9148", "home"))));
        };
    }
}