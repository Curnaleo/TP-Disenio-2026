package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Candidato;
import com.capitalhumano.evaluaciones.modelo.TipoDocumento;

public interface ICandidatoDAO {

    List<Candidato> buscar(String apellido, String nombre, int nro);

    Candidato buscarPorDocumento(TipoDocumento tipo, String nro);

    Candidato findById(int id);

    void guardar(Candidato c);

    void actualizar(Candidato c);

    void eliminarLogico(int id);
}
