package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.Factor;

public interface IFactorDAO {

    List<Factor> buscar(String comp, String nombre);

    Optional<Factor> findByCodigo(String codigo);
    
    void guardar(Factor factor);

    void actualizar(Factor factor);

    void eliminarLogico(String codigo);
}
