package com.alkemy.backend.security.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description="Usuario enviado para ingresar a la API y generar un token",value="AuthenticationRequest",reference="AuthenticationRequest")
public class AuthenticationRequest {

	private String userName;
	private String password;
}
