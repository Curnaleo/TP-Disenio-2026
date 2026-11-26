package com.grupo30.capitalhumano.evaluaciones.repository;

import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class PreguntaRepository implements IPreguntaDAO {

	private final EntityManager entityManager;

	@Override
	public List<Pregunta> buscarPreguntas(String comp, String factor,String nombre) {

		StringBuilder jpql = new StringBuilder(
			"SELECT p " +
			"FROM Pregunta p " +
			"WHERE p.activo = true "
		);

		if (comp != null && !comp.isBlank()) {
			jpql.append(
				"AND UPPER(p.factor.competencia.nombre) " +
				"LIKE UPPER(:comp) "
			);
		}

		if (factor != null && !factor.isBlank()) {
			jpql.append(
				"AND UPPER(p.factor.nombre) " +
				"LIKE UPPER(:factor) "
			);
		}

		if (nombre != null && !nombre.isBlank()) {
			jpql.append(
				"AND UPPER(p.nombre) " +
				"LIKE UPPER(:nombre) "
			);
		}

		jpql.append("ORDER BY p.nombre");

		TypedQuery<Pregunta> query =
			entityManager.createQuery(
				jpql.toString(),
				Pregunta.class
			);

		if (comp != null && !comp.isBlank()) {
			query.setParameter(
				"comp",
				"%" + comp + "%"
			);
		}

		if (factor != null && !factor.isBlank()) {
			query.setParameter(
				"factor",
				"%" + factor + "%"
			);
		}
		
		if (nombre != null && !nombre.isBlank()) {
			query.setParameter(
				"nombre",
				"%" + nombre + "%"
			);
		}

		return query.getResultList();
	}

	@Override
	public Optional<Pregunta> findById(int id) {

		Pregunta pregunta =
			entityManager.find(
				Pregunta.class,
				id
			);

		if (pregunta == null || !Boolean.TRUE.equals(pregunta.getActivo())) {
			return Optional.empty();
		}

		return Optional.of(pregunta);
	}

	@Override
	public void guardar(Pregunta preg) {

		    entityManager.persist(preg);
	}
	
	@Override
	public void actualizar(Pregunta preg) {

		    entityManager.merge(preg);
	}

	@Override
	public void eliminarLogico(int id) {

		findById(id)
			.ifPresent(
				pregunta -> pregunta.setActivo(false)
			);
	}

	@Override
	public List<Pregunta> eliminarPorFactor(String codigoFactor) {

		List<Pregunta> preguntas =
			entityManager.createQuery(
				"SELECT p " +
				"FROM Pregunta p " +
				"WHERE p.factor.codigo = :codigoFactor " +
				"AND p.activo = true",
				Pregunta.class)
			.setParameter(
				"codigoFactor",
				codigoFactor)
			.getResultList();

		preguntas.forEach(
			pregunta -> pregunta.setActivo(false)
		);

		return preguntas;
	}

	@Override
	public List<Pregunta> buscarActivasPorFactor(
        String codigoFactor) {

	    return entityManager.createQuery(
	            """
	            SELECT p
	            FROM Pregunta p
	            WHERE p.activo = true
	            AND p.factor.codigo = :codigoFactor
	            ORDER BY p.nombre
	            """,
	            Pregunta.class
	    )
	    .setParameter(
	            "codigoFactor",
	            codigoFactor
	    )
	    .getResultList();
	}
}
