package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Puesto;

public interface IPuestoDAO {

    List<Puesto> buscar(String codigo, String nombre, String empresa);

    Puesto buscarPorCodigo(String codigoPuesto);

    void guardar(Puesto p);

    void actualizar(Puesto p);

    void eliminarLogico(String codigoPuesto);

    boolean validarEnUso(String codigoPuesto);
}
