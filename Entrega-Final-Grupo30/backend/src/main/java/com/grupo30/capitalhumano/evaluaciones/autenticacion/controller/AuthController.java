package com.grupo30.capitalhumano.evaluaciones.autenticacion.controller;

import com.grupo30.capitalhumano.evaluaciones.autenticacion.dto.AuthResponseDTO;
import com.grupo30.capitalhumano.evaluaciones.autenticacion.dto.LoginCandidatoRequest;
import com.grupo30.capitalhumano.evaluaciones.autenticacion.dto.LoginConsultorRequest;
import com.grupo30.capitalhumano.evaluaciones.autenticacion.security.JwtTokenProvider;
import com.grupo30.capitalhumano.evaluaciones.autenticacion.service.AutenticacionService;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoCuestionarioDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.UsuarioDTO;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

// Controlador REST que expone los endpoints del CU001 - Autenticar usuario.
// Conecta la UI ({@code UIAutenticacion}) con {@link AutenticacionService} ({@code GestorAutenticacion}).

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AutenticacionService autenticacionService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/consultor")
    public ResponseEntity<AuthResponseDTO> autenticarConsultor(
            @Valid @RequestBody LoginConsultorRequest request) {
        UsuarioDTO usuario = autenticacionService.autenticarConsultor(
                request.getUsuario(),
                request.getContrasena());

        String token = jwtTokenProvider.generarTokenConsultor(
                usuario.getIdUsuario(),
                usuario.getNombre());

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .rol("CONSULTOR")
                .consultor(usuario)
                .build());
    }

    @PostMapping("/candidato")
    public ResponseEntity<AuthResponseDTO> autenticarCandidato(
            @Valid @RequestBody LoginCandidatoRequest request) {
                
        CandidatoCuestionarioDTO candidato = autenticacionService.autenticarCandidato(
                request.getTipoDocumento(),
                request.getNroDocumento(),
                request.getClave());
                
        String identificadorDoc = request.getTipoDocumento().name() + "-" + request.getNroDocumento().trim();
        String token = jwtTokenProvider.generarTokenCandidato(
                candidato.getNroCandidato(),
                candidato.getIdCuestionario(),
                identificadorDoc);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .rol("CANDIDATO")
                .candidato(candidato)
                .build());
            }

    @PostMapping("/logout")
    public ResponseEntity<Void> cerrarSesion() {
        autenticacionService.cerrarSesion();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioActual(Authentication authentication) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("usuario", authentication.getName());
        info.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
                
        if (authentication.getCredentials() instanceof Map<?, ?> claims) {
            info.put("claims", claims);
        }

        return ResponseEntity.ok(info);
    }
}
