package com.grupo30.capitalhumano.evaluaciones.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;

@SpringBootTest
@Transactional
class GestorEvaluacionesIT {

	@Autowired
	private GestorEvaluaciones gestorEvaluaciones;

	@Test
	void deberiaGenerarEvaluacion() {

	List<CandidatoGestionDTO> candidatos =
		gestorEvaluaciones.buscarCandidatos(
			"ALVAREZ",
			null,
			null);

		assertFalse(candidatos.isEmpty());

		assertDoesNotThrow(() ->
			gestorEvaluaciones.generarEvaluacion(
				1,
				List.of(candidatos.getFirst())
				));
	}

	@Test
	void deberiaFallarSiNoHayCandidatos() {

		IllegalArgumentException ex =
			assertThrows(
				IllegalArgumentException.class,
				() -> gestorEvaluaciones.generarEvaluacion(
					1,
					List.of()));

		        assertTrue(
				ex.getMessage().contains(
					"al menos un candidato"));
	}

	@Test
	void deberiaFallarSiNoHayPuesto() {

		IllegalArgumentException ex =
			assertThrows(
				IllegalArgumentException.class,
					() -> gestorEvaluaciones.generarEvaluacion(
						null,
						List.of()));

		        assertTrue(
				ex.getMessage().contains(
					"puesto"));
	}

}
