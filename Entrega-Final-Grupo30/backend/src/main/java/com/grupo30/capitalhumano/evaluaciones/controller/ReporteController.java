package com.grupo30.capitalhumano.evaluaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoOrdenMeritoDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.EvaluacionDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.PuestoDTO;
import com.grupo30.capitalhumano.evaluaciones.service.GestorPuestos;
import com.grupo30.capitalhumano.evaluaciones.service.GestorReportes;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

	private final GestorPuestos gestorPuestos;
	private final GestorReportes gestorReportes;

	@GetMapping("/puestos")
	public ResponseEntity<List<PuestoDTO>> buscarPuestos(
			@RequestParam(required = false) String codigo,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String empresa) {
		try {
			return ResponseEntity.ok(gestorPuestos.buscarPuestos(codigo, nombre, empresa));
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/puestos/{idPuesto}/evaluaciones")
	public ResponseEntity<List<EvaluacionDTO>> listarEvaluaciones(@PathVariable Integer idPuesto) {
		try {
			return ResponseEntity.ok(gestorReportes.listarEvaluaciones(idPuesto));
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/puestos/{idPuesto}/orden-merito")
	public ResponseEntity<List<CandidatoOrdenMeritoDTO>> generarOrdenMerito(
			@PathVariable Integer idPuesto,
			@RequestParam(required = false) List<Integer> evaluacionIds) {
		try {
			return ResponseEntity.ok(gestorReportes.generarOrdenMerito(idPuesto, evaluacionIds));
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}
}
