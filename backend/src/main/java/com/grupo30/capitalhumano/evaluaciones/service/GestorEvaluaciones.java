package com.grupo30.capitalhumano.evaluaciones.service;

import java.time.LocalDate;
import java.security.SecureRandom;

import org.springframework.security.core.context.SecurityContextHolder;

import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IUsuarioDAO;

import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;
import com.grupo30.capitalhumano.evaluaciones.entity.CaracteristicaPuesto;
import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestorEvaluaciones {

	private final ICandidatoDAO candidatoDAO;
	private final IPuestoDAO puestoDAO;
	private final IEvaluacionDAO evaluacionDAO;
	private final ICuestionarioDAO cuestionarioDAO;
	private final IUsuarioDAO usuarioDAO;
	private final IFactorDAO factorDAO;
	private final IPreguntaDAO preguntaDAO;

	private Usuario obtenerUsuarioActual() {

		String username =
			SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();

		return usuarioDAO.buscarPorUser(username)
			.orElseThrow(() ->
				new IllegalArgumentException(
				"No existe el usuario autenticado"
			));
	}

	private CandidatoGestionDTO mapToDTO(Candidato candidato) {

		return CandidatoGestionDTO.builder()
			.nroCandidato(candidato.getNroCandidato())
			.apellido(candidato.getApellido())
			.nombre(candidato.getNombre())
			.tipoDocumento(candidato.getTipoDocumento())
			.nroDocumento(candidato.getNroDocumento())
			.email(candidato.getEmail())
			.build();
	}

	@Transactional(readOnly = true)
	public List<CandidatoGestionDTO> buscarCandidatos(
		String apellido,
		String nombre,
		Integer nroCandidato) {

		List<CandidatoGestionDTO> resultado 
			= new ArrayList<>();

		for (Candidato candidato :
			candidatoDAO.buscar(
				apellido,
				nombre,
				nroCandidato)) {

			resultado.add(mapToDTO(candidato));
		}

		return resultado;
	}

	@Transactional(readOnly = true)
	public List<CandidatoGestionDTO> validarCandidatosActivos(
		List<CandidatoGestionDTO> candidatos) {

		List<CandidatoGestionDTO> 
			candidatosConCuestionario =
			new ArrayList<>();

		for (CandidatoGestionDTO candidato : candidatos) {

		cuestionarioDAO.buscarActivoPorCandidato(
		candidato.getNroCandidato())
		.ifPresent(c -> candidatosConCuestionario
				.add(candidato));
		}

		return candidatosConCuestionario;
	}

	private Puesto recuperarPuesto(Integer idPuesto) {

		Puesto puesto = puestoDAO.findById(idPuesto);

		if (puesto == null) {
			throw new IllegalArgumentException(
			"No existe el puesto con ID " + idPuesto
			);
		}

		return puesto;
	}

	@Transactional(readOnly = true)
	private void validarPuestoEvaluable(Integer idPuesto) {

		Puesto puesto = recuperarPuesto(idPuesto);

		List<String> competenciasNoEvaluables = 
			new ArrayList<>();

		for (CaracteristicaPuesto caracteristica :
			puesto.getCaracteristicas()) {

			Competencia competencia =
				caracteristica.getCompetencia();

			boolean competenciaEvaluable = true;

			List<Factor> factores =
				factorDAO.buscarPorCompetencia(
					competencia.getCodigo()
				);

			if (factores.isEmpty()) {
				competenciaEvaluable = false;
			}

			for (Factor factor : factores) {

				List<Pregunta> preguntas =
					preguntaDAO.buscarActivasPorFactor(
					factor.getCodigo()
				);

				if (preguntas.size() < 2) {
					competenciaEvaluable = false;
					break;
				}
			}

			if (!competenciaEvaluable) {
				competenciasNoEvaluables.add(
					competencia.getNombre()
				);
			}
		}

		if (!competenciasNoEvaluables.isEmpty()) {
			throw new IllegalArgumentException(
				"Las siguientes competencias no pueden evaluarse: "
				+ String.join(
				", ",
				competenciasNoEvaluables
				)
			);
		}
	}

	private Evaluacion crearEvaluacion(
		Puesto puesto,
		Usuario usuario) {

		Evaluacion evaluacion =
			new Evaluacion();

		evaluacion.setNombreEvaluacion(
			puesto.getNombre()
		);

		evaluacion.setDescripcion(
			"EVALUACION GENERADA PARA "
			+ puesto.getNombre()
		);

		evaluacion.setFechaCreacion(
			LocalDate.now()
		);

		evaluacion.setActivo(true);

		evaluacion.setPuesto(puesto);

		evaluacion.setUsuario(usuario);

		evaluacionDAO.guardar(evaluacion);

		return evaluacion;
	}

	private static final String ALFABETO =
		        "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final SecureRandom RANDOM =
		        new SecureRandom();

	private String generarClave() {

		StringBuilder clave =
			new StringBuilder();

		for (int i = 0; i < 8; i++) {

			clave.append(
				ALFABETO.charAt(
					RANDOM.nextInt(
						ALFABETO.length()
					)
				)
			);
		}

		return clave.toString();
	}

	private void crearCuestionario(
		Candidato candidato,
		Evaluacion evaluacion) {

		Cuestionario cuestionario =
			Cuestionario.builder()
				.clave(generarClave())
				.estado(EstadoCuestionario.ACTIVO)
				.cantidadAccesos(0)
				.candidato(candidato)
				.evaluacion(evaluacion)
				.build();

		cuestionarioDAO.guardar(cuestionario);
	}

	@Transactional
	public void generarEvaluacion(
		Integer idPuesto,
		List<CandidatoGestionDTO> candidatos) {

		if (idPuesto == null) {
			throw new IllegalArgumentException(
				"Debe seleccionar un puesto"
			);
		}

		if (candidatos == null || candidatos.isEmpty()) {
			throw new IllegalArgumentException(
				"Debe seleccionar al menos un candidato"
			);
		}

		List<CandidatoGestionDTO> candidatosInvalidos =
			validarCandidatosActivos(candidatos);

		if (!candidatosInvalidos.isEmpty()) {
			throw new IllegalArgumentException(
				"Existen candidatos con cuestionarios activos o en proceso: "
				+ candidatosInvalidos.stream()
					.map(c ->
						c.getApellido()
						+ ", "
						+ c.getNombre())
					.toList()
			);
		}

		validarPuestoEvaluable(idPuesto);

		Puesto puesto = recuperarPuesto(idPuesto);

		Usuario usuario = obtenerUsuarioActual();

		Evaluacion evaluacion =
			crearEvaluacion(
				puesto,
				usuario
			);
		
		for (CandidatoGestionDTO dto : candidatos) {

			Candidato candidato =
				candidatoDAO.findById(
					dto.getNroCandidato()
				)
				.orElseThrow(() ->
					new IllegalArgumentException(
						"No existe el candidato "
						+ dto.getNroCandidato()
					)
				);

			crearCuestionario(
				candidato,
				evaluacion
			);
		}
	}
}
