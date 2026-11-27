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

import com.grupo30.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.grupo30.capitalhumano.evaluaciones.service.GestorOpcionesRespuesta;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/opciones-respuesta")
@RequiredArgsConstructor
public class OpcionRespuestaController {

	private final 
		GestorOpcionesRespuesta gestorOpcionesRespuesta;

	@GetMapping
	public List<OpcionRespuestaDTO> buscarOpciones(
		@RequestParam(required = false) String nombre) {

		return gestorOpcionesRespuesta.buscarOpciones(nombre);
	}

	@PostMapping
	public ResponseEntity<Void> altaOpcion(
		@RequestBody OpcionRespuestaDTO opcionDTO) {

		gestorOpcionesRespuesta.altaOpcion(opcionDTO);

		return ResponseEntity
		.status(HttpStatus.CREATED)
		.build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modificarOpcion(
		@PathVariable Integer id,
		@RequestBody OpcionRespuestaDTO opcionDTO) {

		opcionDTO.setId(id);

		gestorOpcionesRespuesta.modificarOpcion(opcionDTO);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> bajaOpcion(
		@PathVariable Integer id) {

		gestorOpcionesRespuesta.bajaOpcion(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/en-uso")
	public Boolean validarEnUso(
		@PathVariable Integer id) {

		return gestorOpcionesRespuesta.validarOpcionEnUso(id);
	}
}
