package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Evaluacion;

public interface IEvaluacionDAO {

    void guardar(Evaluacion evaluacion);

    Evaluacion findById(int id);

    List<Evaluacion> listarPorPuesto(String codigoPuesto);
}
