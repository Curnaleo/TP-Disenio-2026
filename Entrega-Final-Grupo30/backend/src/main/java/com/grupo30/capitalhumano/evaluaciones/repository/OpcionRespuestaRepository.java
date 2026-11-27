package com.grupo30.capitalhumano.evaluaciones.repository;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionRespuestaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class OpcionRespuestaRepository implements IOpcionRespuestaDAO {

	private final EntityManager entityManager;

	@Override
	public List<OpcionRespuesta> buscarOpciones(String nombre) {

		if (nombre == null || nombre.isBlank()) {

			return entityManager.createQuery(
				"SELECT o " +
				"FROM OpcionRespuesta o " +
				"WHERE o.activo = true " +
				"ORDER BY o.nombre",
				OpcionRespuesta.class)
			.getResultList();
		}

		return entityManager.createQuery(
			"SELECT o " +
			"FROM OpcionRespuesta o " +
			"WHERE o.activo = true " +
			"AND UPPER(o.nombre) " +
			"LIKE UPPER(:nombre) " +
			"ORDER BY o.nombre",
			OpcionRespuesta.class)
		.setParameter(
			"nombre",
			"%" + nombre + "%")
		.getResultList();
	}

	@Override
	public boolean existePorNombre(String nombre) {
		return !entityManager.createQuery(
			"SELECT o.id FROM OpcionRespuesta o WHERE UPPER(TRIM(o.nombre)) = UPPER(:nombre)",
			Integer.class)
			.setParameter("nombre", nombre)
			.setMaxResults(1)
			.getResultList().isEmpty();
	}

	@Override
	public Optional<OpcionRespuesta> findById(Integer id) {

		OpcionRespuesta opcion =
			entityManager.find(
				OpcionRespuesta.class,
				id
			);

		if (opcion == null || !Boolean.TRUE.equals( opcion.getActivo())) {
			
			return Optional.empty();
		}

		return Optional.of(opcion);
	}
		    

	@Override
	public void guardar(OpcionRespuesta opcion) {

		    entityManager.persist(opcion);
	}

	@Override
	public void actualizar(OpcionRespuesta opcion) {

		    entityManager.merge(opcion);
	}

	@Override
	public void eliminarLogico(Integer id) {

		findById(id)
		.ifPresent(
			opcion -> opcion.setActivo(false)
		);
	}

	@Override
	public boolean validarEnUso(Integer id) {

		Long cantidad =
			entityManager.createQuery(
				"SELECT COUNT(p) " +
				"FROM Pregunta p " +
				"WHERE p.opcionRespuesta.id = :id " +
				"AND p.activo = true",
				Long.class)
			.setParameter("id", id)
			.getSingleResult();

		return cantidad > 0;
	}

}
