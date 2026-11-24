package com.grupo30.capitalhumano.evaluaciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security para la aplicación.
 * Por ahora permite acceso a los endpoints de la API sin autenticación para facilitar
 * el desarrollo y las pruebas. Deberá actualizarse con JWT/LDAP cuando se implemente
 * el CU001 (Autenticar usuario).
 */

@Configuration 
@EnableWebSecurity 

public class SecurityConfig {

    @Bean // Configura la cadena de filtros de seguridad para la aplicación.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Configura la seguridad HTTP para la aplicación.
        http
            .csrf(csrf -> csrf.disable()) // Desactiva la protección CSRF (Cross-Site Request Forgery) para simplificar el desarrollo y las pruebas.
            .authorizeHttpRequests(auth -> auth // Configura las reglas de autorización para las solicitudes HTTP.
                .requestMatchers("/api/**").permitAll() // Permite el acceso a todos los endpoints de la API sin autenticación.
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud que no coincida con los patrones anteriores.
            );
        return http.build();
    }
}
