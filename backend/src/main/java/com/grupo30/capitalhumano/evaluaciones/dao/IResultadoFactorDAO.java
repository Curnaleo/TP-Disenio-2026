package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoFactor;

public interface IResultadoFactorDAO {

	void guardar(ResultadoFactor resultado);

	List<ResultadoFactor> listarPorCuestionario(
		Integer idCuestionario);
}
