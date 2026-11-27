package com.grupo30.capitalhumano.evaluaciones.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import com.grupo30.capitalhumano.evaluaciones.dto.PreguntaExamenDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.RespuestaCandidatoDTO;

import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoCompetencia;

import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoCompetenciaDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GestorCuestionarioIT {

	@Autowired
	private GestorCuestionario gestorCuestionario;

	@Autowired
	private ICuestionarioDAO cuestionarioDAO;

	@Autowired
	private IResultadoCompetenciaDAO resultadoCompetenciaDAO;

	@Test
	void deberiaFallarSiNoExisteElCuestionario() {

		assertThrows(
			IllegalArgumentException.class,
				() -> gestorCuestionario.iniciarCuestionario(
					Integer.MAX_VALUE));
	}

	@Test
	void deberiaFallarSiNoExisteBloque() {

		assertThrows(
			IllegalArgumentException.class,
				() -> gestorCuestionario.recuperarBloque(
					1,
					999));
	}

	@Test
	void deberiaIniciarCuestionario() {

		assertDoesNotThrow(
			() -> gestorCuestionario
				.iniciarCuestionario(1));
	}

	@Test
	void deberiaPermitirReingresarACuestionarioEnProceso() {

		int bloque =
			gestorCuestionario
				.iniciarCuestionario(1);

		assertTrue(bloque >= 0);

		int bloquePendiente =
			gestorCuestionario
				.iniciarCuestionario(1);

		assertTrue(bloquePendiente >= 0);
	}

	@Test
	void deberiaGuardarLasRespuestasDelBloque() {

		List<PreguntaExamenDTO> preguntas =
			gestorCuestionario.recuperarBloque(
				1,
				1);

		List<RespuestaCandidatoDTO> respuestas =
			preguntas.stream()
				.map(p ->
					RespuestaCandidatoDTO.builder()
						.idPregCuest(
							p.getIdPregCuest())
						.idOpcionPregunta(
							p.getOpciones()
								.getFirst()
								.getIdOpcionPregunta())
						.build())
				.toList();

		assertDoesNotThrow(
			() -> gestorCuestionario.guardarBloque(
				1,
				respuestas));
	}

	@Test
	void deberiaFallarSiFaltanRespuestas() {

		List<RespuestaCandidatoDTO> respuestas =
			List.of();

		assertThrows(
			IllegalArgumentException.class,
			() -> gestorCuestionario.guardarBloque(
				1,
				respuestas));
	}

	@Test
	void deberiaCalcularResultados() {

		assertDoesNotThrow(
			() -> gestorCuestionario.calcularPuntaje(1));

		List<ResultadoCompetencia> resultados =
			resultadoCompetenciaDAO
				.listarPorCuestionario(1);

		assertFalse(resultados.isEmpty());
	}

	@Test
	void deberiaCompletarCuestionario() {

		gestorCuestionario.finalizarCuestionario(1);

		Cuestionario cuestionario =
			cuestionarioDAO.findById(1)
				.orElseThrow();

		assertEquals(
			EstadoCuestionario.COMPLETO,
			cuestionario.getEstado());

		assertNotNull(
			cuestionario.getFechaHoraFin());
	}

	@Test
	void deberiaDetectarTiempoVencido() {

		Cuestionario cuestionario =
			cuestionarioDAO.findById(1)
				.orElseThrow();

		cuestionario.setFechaHoraInicio(
			LocalDateTime.now().minusHours(3));

		cuestionario.setTiempoMaximo(60);

		cuestionarioDAO.actualizar(cuestionario);

		boolean vencido =
			gestorCuestionario
				.verificarTiempoMaximo(
					cuestionario.getIdCuestionario());

		assertTrue(vencido);

		Cuestionario actualizado =
			cuestionarioDAO.findById(
				cuestionario.getIdCuestionario())
					.orElseThrow();

		assertEquals(
			EstadoCuestionario.INCOMPLETO,
				actualizado.getEstado());
	}

	@Test
	void deberiaDetectarAccesosExcedidos() {

		Cuestionario cuestionario =
			cuestionarioDAO.findById(1)
				.orElseThrow();

		cuestionario.setCantidadAccesos(5);
		cuestionario.setMaximoAccesos(5);

		cuestionarioDAO.actualizar(cuestionario);

		boolean excedido =
			gestorCuestionario
				.verificarAccesosMaximos(
					cuestionario.getIdCuestionario());

		assertTrue(excedido);

		Cuestionario actualizado =
			cuestionarioDAO.findById(
				cuestionario.getIdCuestionario())
			.orElseThrow();

		assertEquals(
			EstadoCuestionario.INCOMPLETO,
				actualizado.getEstado());
	}
}
