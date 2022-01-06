package com.alkemy.backend.security.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="usuarios")
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description="Usuario a registrar en la API",value="Usuario",reference="Usuario")
public class Usuario implements Serializable{

	@ApiModelProperty(hidden=true)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message="No puede ser nulo")
	@Size(min=3,message="Debe tener mímnimo 3 letras")
	@Column(name="nombre")
	private String nombre;
	
	@NotNull(message="No puede ser nulo")
	@Size(min=2,message="Debe tener mínimo 2 caracteres")
	@Column(name="nombreUsuario",unique=true)
	@ApiModelProperty(value="Nombre del usuario para ingresar con token.",example="juan",required=true)
	private String nombreUsuario;
	
	@NotNull
	@Email(message="Debe tener formato de email.")
	@Column(name="email",unique=true,nullable=false)
	private String email;
	
	@NotNull
	@Size(min=4,message="Debe tener mínimo 4 caracteres")
	@Column(name="password",nullable=false)
	@ApiModelProperty(value="Password con el que se registró el usuario.",example="3333",required=true)
	private String password;

	public Usuario(@NotNull String nombre, @NotNull String nombreUsuario, @NotNull String email,
			@NotNull String password) {
		super();
		this.nombre = nombre;
		this.nombreUsuario = nombreUsuario;
		this.email = email;
		this.password = password;
	}
	
}
