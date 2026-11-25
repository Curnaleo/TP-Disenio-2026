package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import com.grupo30.capitalhumano.evaluaciones.entity.Competencia;

public interface ICompetenciaDAO {

	List<Competencia> buscar(String nombre);

	Competencia findByCodigo(String codigo);

	void guardar(Competencia competencia);

	void actualizar(Competencia competencia);

	void eliminarLogico(String codigo);
        
	boolean validarEnUso(String codigo);
}
