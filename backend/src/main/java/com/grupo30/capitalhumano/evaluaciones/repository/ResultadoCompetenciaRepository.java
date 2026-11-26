package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoCompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoCompetencia;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class ResultadoCompetenciaRepository implements IResultadoCompetenciaDAO {

	private final EntityManager entityManager;

	@Override
	public void guardar(ResultadoCompetencia resultado) {
		entityManager.persist(resultado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ResultadoCompetencia> listarPorCuestionario(Integer idCuestionario) {
		return entityManager.createQuery("""
			SELECT rc
			FROM ResultadoCompetencia rc
			JOIN FETCH rc.competencia
			WHERE rc.cuestionario.idCuestionario = :idCuestionario
			""", ResultadoCompetencia.class)
			.setParameter("idCuestionario", idCuestionario)
			.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ResultadoCompetencia> listarPorCuestionarios(List<Integer> idsCuestionarios) {
		if (idsCuestionarios == null || idsCuestionarios.isEmpty()) {
			return List.of();
		}

		return entityManager.createQuery("""
			SELECT rc
			FROM ResultadoCompetencia rc
			JOIN FETCH rc.competencia
			JOIN FETCH rc.cuestionario
			WHERE rc.cuestionario.idCuestionario IN :idsCuestionarios
			""", ResultadoCompetencia.class)
			.setParameter("idsCuestionarios", idsCuestionarios)
			.getResultList();
	}
}
