package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.BloquePreguntas;

public interface IBloquePreguntasDAO {

	void guardar(BloquePreguntas bloque);

	void actualizar(BloquePreguntas bloque);

	Optional<BloquePreguntas> findById(Integer id);

	List<BloquePreguntas> recuperarBloquesPorCuestionario(Integer cuestionarioId);
}
