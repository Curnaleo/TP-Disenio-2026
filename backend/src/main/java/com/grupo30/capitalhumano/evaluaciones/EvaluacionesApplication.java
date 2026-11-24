package com.grupo30.capitalhumano.evaluaciones;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvaluacionesApplication {

	public static void main(String[] args) {
		// PostgreSQL puede rechazar el alias que usa Java en Windows.
		if ("America/Buenos_Aires".equals(TimeZone.getDefault().getID())) {
			TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
		}
		SpringApplication.run(EvaluacionesApplication.class, args);
	}

}
