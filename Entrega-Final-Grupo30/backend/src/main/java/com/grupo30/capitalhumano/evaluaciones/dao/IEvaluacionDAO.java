package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.Evaluacion;

public interface IEvaluacionDAO {

	void guardar(Evaluacion evaluacion);

	Optional<Evaluacion> findById(Integer id);

	List<Evaluacion> listarPorPuesto(Integer idPuesto);
}
