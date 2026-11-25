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
        throw new UnsupportedOperationException("Búsqueda de puesto por código pendiente");
    }

    @Override
    public void guardar(Puesto puesto) {
        // TODO: implementar el alta.
    }

    @Override
    public void actualizar(Puesto puesto) {
        // TODO: implementar la actualización.
    }

    @Override
    public void eliminarLogico(String codigo) {
        // TODO: implementar la baja lógica.
    }

    @Override
    public boolean validarEnUso(String codigo) {
        throw new UnsupportedOperationException("Validación de uso del puesto pendiente");
    }

}
