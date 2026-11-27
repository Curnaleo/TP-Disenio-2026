package com.grupo30.capitalhumano.evaluaciones.autenticacion.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;


//Filtro de seguridad que valida el token Bearer JWT en cada solicitud HTTP
//y establece el contexto de autenticación de Spring Security.
 
@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length()).trim();
            if (jwtTokenProvider.validarToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                Map<String, Object> claims = jwtTokenProvider.extraerClaims(token);
                Object subObj = claims.get("sub");
                Object roleObj = claims.get("role");
                if (subObj != null && roleObj != null) {
                    String subject = String.valueOf(subObj);
                    String role = String.valueOf(roleObj);
                    UserDetails userDetails = User.withUsername(subject)
                            .password("")
                            .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)))
                            .build();
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, claims, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
