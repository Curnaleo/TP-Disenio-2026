package com.grupo30.capitalhumano.evaluaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaDTO;
import com.grupo30.capitalhumano.evaluaciones.service.GestorPreguntas;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/preguntas")
@RequiredArgsConstructor
public class PreguntaController {

	private final GestorPreguntas gestorPreguntas;

	@GetMapping
	public List<PreguntaDTO> buscarPreguntas(
		@RequestParam(required = false) String competencia,
		@RequestParam(required = false) String factor,
		@RequestParam(required = false) String nombre) {

		return gestorPreguntas.buscarPreguntas(
			competencia,
			factor,
			nombre
		);
	}

	@PostMapping
	public ResponseEntity<Void> altaPregunta(
		@RequestBody PreguntaDTO preguntaDTO) {

		System.out.println("ENTRO AL POST");

		gestorPreguntas.altaPregunta(preguntaDTO);

		return ResponseEntity
			.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modificarPregunta(
		@PathVariable Integer id,
		@RequestBody PreguntaDTO preguntaDTO) {

		preguntaDTO.setIdPregunta(id);

		gestorPreguntas.modificarPregunta(preguntaDTO);

		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> bajaPregunta(
		@PathVariable Integer id) {

		gestorPreguntas.bajaPregunta(id);

		return ResponseEntity.noContent().build();
	}
}
