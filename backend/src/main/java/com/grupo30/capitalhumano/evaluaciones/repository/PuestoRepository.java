package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Puesto> buscar(String codigo, String nombre, String empresa) {
        throw new UnsupportedOperationException("Búsqueda de puestos pendiente");
    }

    @Override
    public Puesto findByCodigo(String codigo) {
	List<Puesto> resultados =
		entityManager.createQuery(
			"SELECT p " +
			"FROM Puesto p " +
			"WHERE p.codigo = :codigo " +
			"AND p.activo = true",
			Puesto.class)
		.setParameter("codigo", codigo)
		.getResultList();

	return resultados.isEmpty()
		? null
		: resultados.getFirst();
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
    public void eliminarLogico(String codigo) {
        Puesto puesto = findByCodigo(codigo);

        if (puesto != null) {
	        puesto.setActivo(false);
	}
    }

    @Override
    public boolean validarEnUso(String codigo) {
        throw new UnsupportedOperationException("Validación de uso del puesto pendiente");
    }

}
