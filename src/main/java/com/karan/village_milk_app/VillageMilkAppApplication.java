package com.karan.village_milk_app;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class VillageMilkAppApplication {
    @Autowired
    private Environment environment;

    @PostConstruct
    public void testProfiles() {
        System.out.println("ACTIVE PROFILES = " +
                Arrays.toString(environment.getActiveProfiles()));
    }

	public static void main(String[] args) {
		SpringApplication.run(VillageMilkAppApplication.class, args);
	}


}
