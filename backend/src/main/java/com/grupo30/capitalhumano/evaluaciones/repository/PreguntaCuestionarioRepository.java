package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IPreguntaCuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.PreguntaCuestionario;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class PreguntaCuestionarioRepository implements IPreguntaCuestionarioDAO {

	private final EntityManager entityManager;

	@Override
	public void guardar(PreguntaCuestionario preguntaCuestionario) {

		entityManager.persist(preguntaCuestionario);
	}

	@Override
	public void actualizar(PreguntaCuestionario preguntaCuestionario) {

		entityManager.merge(preguntaCuestionario);
	}

	@Override
	public List<PreguntaCuestionario> recuperarPorBloque(Integer bloqueId) {

		return entityManager.createQuery("""
			SELECT p
			FROM PreguntaCuestionario p
			WHERE p.bloque.idBloque = :idBloque
			ORDER BY p.nroOrden
			""", PreguntaCuestionario.class)
		.setParameter("idBloque", bloqueId)
		.getResultList();
	}
}
