package com.grupo30.capitalhumano.evaluaciones.autenticacion.service;
import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoCuestionarioDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.UsuarioDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

// Interfaz del servicio de autenticación (GestorAutenticacion en el diagrama de clases).
// Corresponde al CU001 - Autenticar usuario.


public interface AutenticacionService {
    UsuarioDTO autenticarConsultor(String user, String pass);
    CandidatoCuestionarioDTO autenticarCandidato(TipoDocumento tipo, String nro, String clave);
    void cerrarSesion();
}