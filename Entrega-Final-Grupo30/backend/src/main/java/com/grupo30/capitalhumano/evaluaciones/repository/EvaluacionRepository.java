package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IEvaluacionDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class EvaluacionRepository implements IEvaluacionDAO {

	private final EntityManager entityManager;

	@Override
	public void guardar(Evaluacion evaluacion) {

		    entityManager.persist(evaluacion);
	}

	@Override
	public Optional<Evaluacion> findById(Integer id) {

		Evaluacion evaluacion =
			entityManager.find(
				Evaluacion.class,
				id);

		if (evaluacion == null) {
			return Optional.empty();
		}

		return Optional.of(evaluacion);
	}

	@Override
	public List<Evaluacion> listarPorPuesto(Integer idPuesto) {

		return entityManager.createQuery(
			"SELECT e " +
			"FROM Evaluacion e " +
			"WHERE e.puesto.puestoId = :idPuesto " +
			"AND e.activo = true " +
			"ORDER BY e.fechaCreacion DESC",
			Evaluacion.class)
		.setParameter(
			"idPuesto",
			idPuesto)
		.getResultList();
	}
}
