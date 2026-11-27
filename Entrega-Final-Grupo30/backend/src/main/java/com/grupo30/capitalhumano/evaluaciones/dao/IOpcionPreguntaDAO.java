package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.OpcionPregunta;

public interface IOpcionPreguntaDAO {

	Optional<OpcionPregunta> findById(Integer id);

}
