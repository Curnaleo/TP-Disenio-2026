package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.BloquePreguntas;

public interface IBloquePreguntasDAO {

    void guardar(BloquePreguntas bloque);

    BloquePreguntas findById(int id);

    List<BloquePreguntas> recuperarBloquesPorCuestionario(int cuestionarioId);
}
