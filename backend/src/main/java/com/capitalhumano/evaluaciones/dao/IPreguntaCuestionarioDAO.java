package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.PreguntaCuestionario;

public interface IPreguntaCuestionarioDAO {

    void guardar(PreguntaCuestionario p);

    void actualizar(PreguntaCuestionario p);

    List<PreguntaCuestionario> recuperarPorBloque(int bloqueId);
}
