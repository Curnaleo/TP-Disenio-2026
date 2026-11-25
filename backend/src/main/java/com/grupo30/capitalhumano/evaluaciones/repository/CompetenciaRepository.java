package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICompetenciaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class CompetenciaRepository implements ICompetenciaDAO {

	private final EntityManager entityManager;

	@Override
	public void guardar(Competencia competencia) {

		entityManager.persist(competencia);
	}

	@Override
	public void actualizar(Competencia competencia) {

		entityManager.merge(competencia);
	}

	@Override
	public void eliminarLogico(String codigo) {
		findByCodigo(codigo).ifPresent(competencia -> competencia.setActivo(false));
	}

	@Override
	public List<Competencia> buscar(String nombre) {

		if (nombre == null || nombre.isBlank()) {

			return entityManager.createQuery(
			"SELECT c " +
			"FROM Competencia c " +
			"WHERE c.activo = true " +
			"ORDER BY c.nombre",
			Competencia.class)
		.getResultList();
		}

		return entityManager.createQuery(
			"SELECT c " +
			"FROM Competencia c " +
			"WHERE c.activo = true " +
			"AND UPPER(c.nombre) LIKE UPPER(:nombre) " +
			"ORDER BY c.nombre",
			Competencia.class)
		.setParameter("nombre", "%" + nombre + "%")
		.getResultList();
	}

	@Override
	public Optional<Competencia> findByCodigo(String codigo) {

		List<Competencia> resultados =
		entityManager.createQuery(
			"SELECT c " +
			"FROM Competencia c " +
			"WHERE c.codigo = :codigo " +
			"AND c.activo = true",
			Competencia.class)
		.setParameter("codigo", codigo)
		.getResultList();
	        
		return resultados.isEmpty()
			? Optional.empty()
			: Optional.of(resultados.getFirst());
	}

	@Override
	public boolean validarEnUso(String codigo) {

		Long cantidadFactores = entityManager.createQuery(
			"SELECT COUNT(f) " +
			"FROM Factor f " +
			"WHERE f.competencia.codigo = :codigo " +
			"AND f.activo = true",
			Long.class)
		.setParameter("codigo", codigo)
		.getSingleResult();

		Long cantidadPuestos = entityManager.createQuery(
			"SELECT COUNT(cp) " +
			"FROM CaracteristicaPuesto cp " +
			"WHERE cp.competencia.codigo = :codigo",
			Long.class)
		.setParameter("codigo", codigo)
		.getSingleResult();

		return cantidadFactores > 0 || cantidadPuestos > 0;
	}
} 
