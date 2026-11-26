package com.grupo30.capitalhumano.evaluaciones.service;

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

	@Transactional(readOnly = true)
	public List<CandidatoGestionDTO> 
		validarCandidatosActivos(
		List<CandidatoGestionDTO> candidatos) {

		List<CandidatoGestionDTO> 
			candidatosConCuestionario = 
			new ArrayList<>();

		for (CandidatoGestionDTO candidato : candidatos) {

		Optional<Cuestionario> cuestionario =
			cuestionarioDAO.buscarActivoPorCandidato(
				candidato.getNroCandidato()
			);

		if (cuestionario.isPresent()) {
			candidatosConCuestionario.add(candidato);
			}
		}

		return candidatosConCuestionario;
	}
}
