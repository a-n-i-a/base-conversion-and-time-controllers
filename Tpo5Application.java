package com.example.tpo5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Time;
import java.time.ZonedDateTime;

@SpringBootApplication
public class Tpo5Application {

	public static void main(String[] args) {
		SpringApplication.run(Tpo5Application.class, args);
		System.out.println(ZonedDateTime.now());
	}

}
