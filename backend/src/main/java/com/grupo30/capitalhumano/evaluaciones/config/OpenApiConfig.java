package com.grupo30.capitalhumano.evaluaciones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	private static final String SECURITY_SCHEME_NAME =
		"bearerAuth";

	@Bean
	public OpenAPI customOpenAPI() {

		SecurityScheme securityScheme =
			new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.description("Ingrese el token JWT obtenido desde los endpoints de autenticación.");

		return new OpenAPI()
			.info(new Info()
				.title("Capital Humano API")
				.version("1.0")
				.description("API para la gestión de evaluaciones de perfiles.")
			)
			.addSecurityItem(
				new SecurityRequirement()
				.addList(SECURITY_SCHEME_NAME)
			)
			.components(
				new Components()
					.addSecuritySchemes(
						SECURITY_SCHEME_NAME,
						securityScheme
					)
				);
	}
}
