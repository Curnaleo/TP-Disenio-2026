package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.TypedQuery;

import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class PuestoRepository implements IPuestoDAO {

    private final EntityManager entityManager;

    @Override
    public boolean validarEnUso(int puestoId) {

	Long cantidadEvaluaciones =
	entityManager.createQuery(
	"SELECT COUNT(e) " +
	"FROM Evaluacion e " +
	"WHERE e.puesto.id = :puestoId " +
	"AND e.activo = true",
	Long.class)
	.setParameter("puestoId", puestoId)
	.getSingleResult();

	return cantidadEvaluaciones > 0;
    }

    @Override
    public Puesto findById(int puestoId) {
        Puesto puesto = entityManager.find(Puesto.class, puestoId);
        if (puesto == null || !Boolean.TRUE.equals(puesto.getActivo())) {
            return null;
        }
        return puesto;
    }

    @Override
    public void guardar(Puesto puesto) {
        
	entityManager.persist(puesto);
    }

    @Override
    public void actualizar(Puesto puesto) {
        
	    entityManager.merge(puesto);
    }

    @Override
    public void eliminarLogico(int puestoId) {
        Puesto puesto = findById(puestoId);

        if (puesto != null) {
	        puesto.setActivo(false);
	}
    }

    @Override
    public List<Puesto> buscar(
	String codigo,
	String nombre,
	String empresa) {

	StringBuilder jpql = new StringBuilder(
		"SELECT p " +
		"FROM Puesto p " +
		"WHERE p.activo = true "
	);

	if (codigo != null && !codigo.isBlank()) {
		jpql.append(
		"AND UPPER(p.codigo) " +
		"LIKE UPPER(:codigo) "
		);
	}

	if (nombre != null && !nombre.isBlank()) {
		jpql.append(
		"AND UPPER(p.nombre) " +
		"LIKE UPPER(:nombre) "
		);
	}

	if (empresa != null && !empresa.isBlank()) {
		jpql.append(
		"AND UPPER(p.empresa) " +
		"LIKE UPPER(:empresa) "
		);
	}

	jpql.append("ORDER BY p.nombre");
		TypedQuery<Puesto> query =
		entityManager.createQuery(
			jpql.toString(),
			Puesto.class
			);

		if (codigo != null && !codigo.isBlank()) {
			query.setParameter(
			"codigo",
			"%" + codigo + "%"
			);
		}

		if (nombre != null && !nombre.isBlank()) {
			query.setParameter(
			"nombre",
			"%" + nombre + "%"
			);
		}

		if (empresa != null && !empresa.isBlank()) {
			query.setParameter(
			"empresa",
			"%" + empresa + "%"
			);
		}

		return query.getResultList();
	}
}
