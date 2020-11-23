package com.bonsainet.species;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
// @ServletComponentScan
public class SpeciesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeciesApplication.class, args);
    }

}
