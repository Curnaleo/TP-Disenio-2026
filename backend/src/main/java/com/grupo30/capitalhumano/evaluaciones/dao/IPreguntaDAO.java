package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import java.util.Optional;
import com.grupo30.capitalhumano.evaluaciones.entity.Pregunta;

public interface IPreguntaDAO {

    List<Pregunta> buscarPreguntas(String comp, String factor, String nombre);

    Optional<Pregunta> findById(int id);

    void guardar(Pregunta preg);

    void actualizar(Pregunta preg);

    void eliminarLogico(int id);

    List<Pregunta> eliminarPorFactor(int idFactor);
}
