package com.alkemy.backend.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo()).securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.alkemy.backend.controladores"))
				.paths(PathSelectors.any()).build();
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Api de Disney",
				"Api que permite dar de alta,baja y modificar peliculas y personajes de disney previa auntenticación de usuario.\n"
					 + "Todas las operaciones requieren de un token para operar.\n"
						+ "Para generar un token se debe registrar con usuario,nombreUsuario,email,password.Recibirá un mail de confirmación.\n"
						+ "Para autenticarse se debe enviar nombreUsuario y password."
						+ "IMPORTANTE: una vez generado el token,para enviarlo en las peticiones se debe anteponer la palabra 'Bearer' seguida del token generado en el botón Authorize.Ejemplo:Bearer miToken.\n"
						+ "El token tiene una duración de 8 minutos. Transcurrido ese tiempo se debe hacer LOGOUT y generar otro token.\n",
				   "2.0", "Términos y Condiciones", new Contact("Ricardo Ledesma", "", "sebastianledesma1992@gmail.com"),
				"", "", Collections.emptyList());
	}
}
