package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IFactorDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Factor;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class FactorRepository implements IFactorDAO {

	    private final EntityManager entityManager;

	@Override
	public List<Factor> buscar(
		String comp,
		String nombre) {

		StringBuilder jpql = new StringBuilder(
			"SELECT f " +
			"FROM Factor f " +
			"WHERE f.activo = true "
		);

		if (comp != null && !comp.isBlank()) {
			jpql.append(
				"AND UPPER(f.competencia.nombre) " +
				"LIKE UPPER(:comp) "
			);
		}

		if (nombre != null && !nombre.isBlank()) {
			jpql.append(
				"AND UPPER(f.nombre) " +
				"LIKE UPPER(:nombre) "
			);
		}
		
		jpql.append("ORDER BY f.nombre");
			
		var query =
			entityManager.createQuery(
				jpql.toString(),
				Factor.class
			);

		if (comp != null && !comp.isBlank()) {
			query.setParameter(
				"comp",
				"%" + comp + "%"
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
	public Optional<Factor> findByCodigo(String codigo) {

		List<Factor> resultados =
			entityManager.createQuery(
				"SELECT f " +
				"FROM Factor f " +
				"WHERE f.codigo = :codigo " +
				"AND f.activo = true",
				Factor.class)
			.setParameter("codigo", codigo)
			.getResultList();

		return resultados.isEmpty()
			? Optional.empty()
			: Optional.of(resultados.getFirst());
	}

	@Override
	public void guardar(Factor factor) {

		entityManager.persist(factor);
	}


	@Override
	public void actualizar(Factor factor) {

		entityManager.merge(factor);
	}

	@Override
	public void eliminarLogico(String codigo) {
	
		findByCodigo(codigo)
		.ifPresent(
			factor -> factor.setActivo(false)
		);
	}
}
