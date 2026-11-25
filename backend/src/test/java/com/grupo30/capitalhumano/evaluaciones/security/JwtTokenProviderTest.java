package com.grupo30.capitalhumano.evaluaciones.security;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            new ObjectMapper(),
            "ClaveSecretaDePruebaParaTestsUnitarios2026!",
            3600000L);

    @Test
    void generarYValidarTokenConsultor_extraeClaimsCorrectos() {
        String token = jwtTokenProvider.generarTokenConsultor(7, "CONSULTOR1");

        assertThat(jwtTokenProvider.validarToken(token)).isTrue();

        Map<String, Object> claims = jwtTokenProvider.extraerClaims(token);
        assertThat(claims.get("sub")).isEqualTo("CONSULTOR1");
        assertThat(claims.get("role")).isEqualTo("CONSULTOR");
        assertThat(((Number) claims.get("idUsuario")).intValue()).isEqualTo(7);
    }

    @Test
    void generarYValidarTokenCandidato_extraeClaimsCorrectos() {
        String token = jwtTokenProvider.generarTokenCandidato(100, 55, "DNI-40123456");

        assertThat(jwtTokenProvider.validarToken(token)).isTrue();

        Map<String, Object> claims = jwtTokenProvider.extraerClaims(token);
        assertThat(claims.get("sub")).isEqualTo("DNI-40123456");
        assertThat(claims.get("role")).isEqualTo("CANDIDATO");
        assertThat(((Number) claims.get("nroCandidato")).intValue()).isEqualTo(100);
        assertThat(((Number) claims.get("idCuestionario")).intValue()).isEqualTo(55);
    }

    @Test
    void validarToken_conFirmaAlterada_retornaFalse() {
        String token = jwtTokenProvider.generarTokenConsultor(7, "CONSULTOR1");
        String alterado = token.substring(0, token.length() - 2) + "xx";

        assertThat(jwtTokenProvider.validarToken(alterado)).isFalse();
    }

    @Test
    void validarToken_cuandoEstaExpirado_retornaFalse() {
        JwtTokenProvider expirado = new JwtTokenProvider(
                new ObjectMapper(),
                "ClaveSecretaDePruebaParaTestsUnitarios2026!",
                -10000L);

        String token = expirado.generarTokenConsultor(7, "CONSULTOR1");

        assertThat(expirado.validarToken(token)).isFalse();
    }
}
