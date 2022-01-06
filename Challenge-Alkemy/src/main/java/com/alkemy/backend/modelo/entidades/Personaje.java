package com.alkemy.backend.modelo.entidades;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
@Entity(name="personajes")
@ApiModel(description="Personajes",value="Personaje",reference="Personaje")
public class Personaje {

	@ApiModelProperty(hidden=true)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ApiModelProperty(hidden=true)
	@Column(name="imagen",updatable=true)
	private String imagen;
	
	@NotNull(message="no puede ser nulo.")
	@Size(min=1,message="debe tener mínimo una letra")
	@Column(name="nombre",nullable=false,length=50,updatable=true)
	private String nombre;
	
	@Positive(message="debe ser mayor a 0.")
	@Column(name="edad",nullable=false,updatable=true)
	private Integer edad;
	
	@NotNull(message="no puede ser nulo.")
	@Positive(message="debe ser mayor a 0.")
	@Column(name="peso",nullable=true,updatable=true)
	private Double peso;
	
	@ApiModelProperty(value="Breve descripción o historia del personaje.",required=true)
	@Column(name="historia",nullable=false,updatable=true,length=150)
	private String historia;
	
	@ManyToMany(fetch=FetchType.LAZY,cascade= {CascadeType.MERGE,CascadeType.PERSIST})
	@JoinTable(
			name="personaje_pelicula",
			joinColumns=@JoinColumn(name="personaje_id",foreignKey=@ForeignKey(name="fk_personaje")),
			inverseJoinColumns=@JoinColumn(name="pelicula_id",foreignKey=@ForeignKey(name="fk_pelicula"))
	)
	@JsonIgnoreProperties({"hibernateLazyInitializer","personajes"})
	private Set<Pelicula> peliculas;
	
	
	public Personaje(Integer id, String imagen, String nombre, Integer edad, Double peso, String historia) {
		this();
		this.id = id;
		this.imagen = imagen;
		this.nombre = nombre;
		this.edad = edad;
		this.peso = peso;
		this.historia = historia;
	}
	
	
	public Personaje(Integer id, String imagen, String nombre, Integer edad, Double peso, String historia,Set<Pelicula> peliculas) {
		this(id,imagen,nombre,edad,peso,historia);
		this.peliculas = peliculas;
	}
	
}
