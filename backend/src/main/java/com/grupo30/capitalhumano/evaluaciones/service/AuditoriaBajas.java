package com.grupo30.capitalhumano.evaluaciones.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.grupo30.capitalhumano.evaluaciones.dao.IRegistroAuditoriaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.RegistroAuditoria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditoriaBajas {

    private final IRegistroAuditoriaDAO repositorio;

    public void registrar(String entidad, Integer id) {
        // El usuario se toma de la sesión autenticada.
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("Se requiere un usuario autenticado para registrar la baja");
        }

        repositorio.guardar(RegistroAuditoria.builder()
            .usuario(autenticacion.getName())
            .fechaHora(LocalDateTime.now())
            .accion("BAJA_LOGICA")
            .entidad(entidad)
            .idEntidad(id)
            .build());
    }
}
