package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.OpcionRespuesta;

public interface IOpcionRespuestaDAO {

    List<OpcionRespuesta> buscarOpciones(String nombre);

    OpcionRespuesta findById(int id);

    void guardar(OpcionRespuesta op);

    void actualizar(OpcionRespuesta op);

    void eliminarLogico(int id);

    boolean validarEnUso(int id);
}
