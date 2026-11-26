package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import com.grupo30.capitalhumano.evaluaciones.entity.ResultadoCompetencia;

public interface IResultadoCompetenciaDAO {

	void guardar(ResultadoCompetencia resultado);

	List<ResultadoCompetencia> listarPorCuestionario(Integer idCuestionario);

	List<ResultadoCompetencia> listarPorCuestionarios(List<Integer> idsCuestionarios);
}
