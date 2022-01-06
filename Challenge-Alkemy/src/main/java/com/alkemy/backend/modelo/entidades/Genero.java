package com.alkemy.backend.modelo.entidades;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name="genero")
@ApiModel(description="Géneros de disney",value="Genero",reference="Genero")
public class Genero {

	@ApiModelProperty(hidden=true)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message="no puede ser nulo")
	@Size(min=2,message="debe tener mínimo 2 caracteres")
	@Column(name="nombre",nullable=false,unique=true,length=25)
	private String nombre;
	
	@ApiModelProperty(hidden=true)
	private String imagen;
	
	@OneToMany(mappedBy="genero",fetch=FetchType.LAZY,cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	@JsonIgnoreProperties({"hibernateLazyInitializer","personajes"})
	private Set<Pelicula> peliculas;
	
	public Genero(Integer id, String nombre, String imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.imagen = imagen;
	}

	public Genero(Integer id, String nombre, String imagen, Set<Pelicula> peliculas) {
		this(id,nombre,imagen);
		this.peliculas = peliculas;
	}
	
	
	
}
