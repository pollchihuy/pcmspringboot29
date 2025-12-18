package com.juaracoding.pcmspringboot29;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Pcmspringboot29Application {

	public static void main(String[] args) {
		SpringApplication.run(Pcmspringboot29Application.class, args);
	}

}
