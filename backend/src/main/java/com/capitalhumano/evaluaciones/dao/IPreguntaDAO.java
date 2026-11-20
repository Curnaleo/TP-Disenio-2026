package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Pregunta;

public interface IPreguntaDAO {

    List<Pregunta> buscarPreguntas(String comp, String factor, String nombre);

    Pregunta findById(int id);

    void guardar(Pregunta preg);

    void actualizar(Pregunta preg);

    void eliminarLogico(int id);

    List<Pregunta> eliminarPorFactor(String codigoFactor);
}
