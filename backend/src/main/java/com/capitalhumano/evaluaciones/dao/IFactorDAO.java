package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Factor;

public interface IFactorDAO {

    List<Factor> buscar(String comp, String nombre);

    Factor buscarPorCodigo(String codigoFactor);

    void guardar(Factor factor);

    void actualizar(Factor factor);

    void eliminarLogico(String codigoFactor);
}
