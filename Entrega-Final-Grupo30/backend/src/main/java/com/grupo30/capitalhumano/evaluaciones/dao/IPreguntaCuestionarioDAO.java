package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import com.grupo30.capitalhumano.evaluaciones.entity.PreguntaCuestionario;

public interface IPreguntaCuestionarioDAO {

	void guardar(PreguntaCuestionario pregunta);

	void actualizar(PreguntaCuestionario pregunta);

	List<PreguntaCuestionario> recuperarPorBloque(Integer bloqueId);
}
