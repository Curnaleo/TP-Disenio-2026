package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IBloquePreguntasDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.BloquePreguntas;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class BloquePreguntasRepository implements IBloquePreguntasDAO {

	private final EntityManager entityManager;

	@Override
	public void guardar(BloquePreguntas bloque) {
		entityManager.persist(bloque);
	}

	@Override
	public Optional<BloquePreguntas> findById(Integer id) {

		return Optional.ofNullable(
			entityManager.find(
				BloquePreguntas.class,
				id));
	}

	@Override
	public List<BloquePreguntas> recuperarBloquesPorCuestionario(Integer cuestionarioId) {

		return entityManager.createQuery("""
			SELECT b
			FROM BloquePreguntas b
			WHERE b.cuestionario.idCuestionario = :id
			ORDER BY b.nroBloque
			""", BloquePreguntas.class)
		.setParameter("id", cuestionarioId)
		.getResultList();
	}
}
