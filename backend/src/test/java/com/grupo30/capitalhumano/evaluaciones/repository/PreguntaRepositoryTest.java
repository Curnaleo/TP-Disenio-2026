package com.grupo30.capitalhumano.evaluaciones.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;

@SpringBootTest
@Transactional
class PreguntaRepositoryTest {

	@Autowired
	private PreguntaRepository preguntaRepository;

	@Autowired
	private FactorRepository factorRepository;

	@Autowired
	private CompetenciaRepository competenciaRepository;

	@Autowired
	private OpcionRespuestaRepository opcionRespuestaRepository;

	private Pregunta crearPregunta() {

		Competencia competencia = new Competencia();
		competencia.setCodigo("COMP_TEST");
		competencia.setNombre("COMPETENCIA TEST");
		competencia.setDescripcion("DESC");
		competencia.setActivo(true);
        
		competenciaRepository.guardar(competencia);
	
		Factor factor = new Factor();
		factor.setCodigo("FACT_TEST");
		factor.setNombre("FACTOR TEST");
		factor.setDescripcion("DESC");
		factor.setNroOrden(1);
		factor.setActivo(true);
		factor.setCompetencia(competencia);
	
		factorRepository.guardar(factor);
	
		OpcionRespuesta opcionRespuesta =
		new OpcionRespuesta();
		opcionRespuesta.setNombre("ESCALA TEST");
		opcionRespuesta.setDescripcion("DESC");
		opcionRespuesta.setActivo(true);
	
		opcionRespuestaRepository.guardar(
		opcionRespuesta);
	
		Pregunta pregunta = new Pregunta();
	
		pregunta.setNombre("PREGUNTA TEST");
		pregunta.setTextoPregunta("TEXTO");
		pregunta.setDescripcion("DESC");
		pregunta.setActivo(true);
		pregunta.setFactor(factor);
		pregunta.setOpcionRespuesta(opcionRespuesta);
	
		return pregunta;
	}

	@Test
	void debeGuardarYRecuperarPregunta() {

		Pregunta pregunta = crearPregunta();

		preguntaRepository.guardar(pregunta);

		var recuperada =
		preguntaRepository.findById(
			pregunta.getId());

		assertTrue(recuperada.isPresent());
	}

	@Test
	void debeBuscarPreguntas() {

		var preguntas =
			preguntaRepository.buscarPreguntas(
				"",
				"",
				"");

		assertNotNull(preguntas);
	}

	@Test
	void debeEliminarLogicamentePregunta() {

		Pregunta pregunta = crearPregunta();

		preguntaRepository.guardar(pregunta);

		Integer id = pregunta.getId();

		preguntaRepository.eliminarLogico(id);

		var recuperada =
			preguntaRepository.findById(id);

		assertTrue(recuperada.isEmpty());
	}

	/*@Test
	void debeEliminarTodasLasPreguntasDelFactor() {

		Pregunta pregunta1 = crearPregunta();

		Pregunta pregunta2 = crearPregunta();

		pregunta2.setNombre("PREGUNTA TEST 2");

		pregunta2.setTextoPregunta("TEXTO TEST 2");

		preguntaRepository.guardar(pregunta1);

		preguntaRepository.guardar(pregunta2);

		String codigoFactor =
			pregunta1.getFactor().getCodigo();

			preguntaRepository.eliminarPorFactor(codigoFactor);

		assertTrue(preguntaRepository
				.findById(pregunta1.getId())
				.isEmpty()
			  );

		assertTrue(
			preguntaRepository.findById(
				pregunta2.getId())
			.isEmpty()
		);
	}*/
}
