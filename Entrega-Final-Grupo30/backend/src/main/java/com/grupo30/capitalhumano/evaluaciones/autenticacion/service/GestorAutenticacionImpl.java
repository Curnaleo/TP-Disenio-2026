package com.grupo30.capitalhumano.evaluaciones.autenticacion.service;

import com.grupo30.capitalhumano.evaluaciones.autenticacion.exception.AutenticacionException;
import com.grupo30.capitalhumano.evaluaciones.autenticacion.ldap.LdapAuthService;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoCuestionarioDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.UsuarioDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;
import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;
import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IUsuarioDAO;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Implementación de {@link AutenticacionService} (GestorAutenticacion en el diagrama de clases).
// Implementa el CU001 - Autenticar usuario para Consultores (LDAP) y Candidatos (Doc + Clave).

@Service
@RequiredArgsConstructor
public class GestorAutenticacionImpl implements AutenticacionService {

    private static final int MAX_LEN_USUARIO = 15;
    private static final int MAX_LEN_PASSWORD = 20;
    private static final String MSG_ERROR_CONSULTOR = "El usuario o la contraseña no son válidos";
    private static final String MSG_ERROR_CANDIDATO = "Los datos ingresados no son válidos o no existe un cuestionario para el Candidato";
    
    private final IUsuarioDAO usuarioRepository;
    private final ICandidatoDAO candidatoRepository;
    private final ICuestionarioDAO cuestionarioRepository;
    private final LdapAuthService ldapAuthService;

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO autenticarConsultor(String user, String pass) {
        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            throw new AutenticacionException(MSG_ERROR_CONSULTOR);
        }
        // Requerimiento especial CU001: identificador SIEMPRE en mayúsculas y máx 15 chars; pass máx 20.
        String userNormalizado = user.trim().toUpperCase();
        if (userNormalizado.length() > MAX_LEN_USUARIO || pass.length() > MAX_LEN_PASSWORD) {
            throw new AutenticacionException(MSG_ERROR_CONSULTOR);
        }
        if (!ldapAuthService.autenticar(userNormalizado, pass)) {
            throw new AutenticacionException(MSG_ERROR_CONSULTOR);
        }
        Usuario usuario = usuarioRepository.buscarPorUser(userNormalizado)
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .orElseThrow(() -> new AutenticacionException(MSG_ERROR_CONSULTOR));
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .activo(usuario.getActivo())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CandidatoCuestionarioDTO autenticarCandidato(TipoDocumento tipo, String nro, String clave) {
        if (tipo == null || nro == null || nro.isBlank() || clave == null || clave.isBlank()) {
            throw new AutenticacionException(MSG_ERROR_CANDIDATO);
        }
        String nroNormalizado = nro.trim();
        Candidato candidato = candidatoRepository.buscarPorDocumento(tipo, nroNormalizado)
                .orElseThrow(() -> new AutenticacionException(MSG_ERROR_CANDIDATO));
        Cuestionario cuestionario = cuestionarioRepository
                .buscarPorClaveYDoc(tipo, nroNormalizado, clave)
                .filter(q -> q.getCandidato().getNroCandidato().equals(candidato.getNroCandidato()))
                .orElseThrow(() -> new AutenticacionException(MSG_ERROR_CANDIDATO));
        return CandidatoCuestionarioDTO.builder()
                .idCuestionario(cuestionario.getIdCuestionario())
                .nroCandidato(candidato.getNroCandidato())
                .apellido(candidato.getApellido())
                .nombre(candidato.getNombre())
                .estado(cuestionario.getEstado())
                .build();
    }

    @Override
    public void cerrarSesion() {
        SecurityContextHolder.clearContext();
    }
}

