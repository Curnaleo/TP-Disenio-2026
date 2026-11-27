package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IResultadoFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoFactor;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class ResultadoFactorRepository
        implements IResultadoFactorDAO {

	private final EntityManager entityManager;

	@Override
	public void guardar(
		ResultadoFactor resultado) {

			entityManager.persist(resultado);
	}

	@Override
	public List<ResultadoFactor> listarPorCuestionario(
		Integer idCuestionario) {

		return entityManager.createQuery(
			"""
			SELECT rf
			FROM ResultadoFactor rf
			WHERE rf.cuestionario.idCuestionario =
			:idCuestionario
			""",
			ResultadoFactor.class)
			.setParameter(
				"idCuestionario",
				idCuestionario)
			.getResultList();
	}
}
