package com.legacycorp.bikehubb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BikehubbApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikehubbApplication.class, args);

		System.out.println("Hello, BikeHubb!");
	}

}
