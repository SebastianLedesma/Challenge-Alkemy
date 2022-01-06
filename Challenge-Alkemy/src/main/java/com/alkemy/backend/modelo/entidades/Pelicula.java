package com.alkemy.backend.modelo.entidades;

import java.time.LocalDateTime;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@Entity(name="peliculas")
@ApiModel(description="Peliculas",value="Pelicula",reference="Pelicula")
public class Pelicula {

	@ApiModelProperty(hidden=true)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ApiModelProperty(hidden=true)
	@Column(name="imagen",updatable=true)
	private String imagen;
	
	@NotNull(message="no puede ser nulo")
	@Size(message="debe tener mínimo una letra",min=1)
	@Column(name="titulo",updatable=true,length=80,unique=true,nullable=false)
	private String titulo;
	
	@ApiModelProperty(hidden=true)
	@Column(name="fecha_creacion",updatable=true)
	private LocalDateTime fechaCreacion;
	
	@Min(value=1,message="debe ser mayor o igual a 1")
	@Max(value=5,message="debe ser menor a igual a 5")
	@ApiModelProperty(value="Calificación de la película.",required=true)
	@Column(name="calificacion")
	private Integer calificacion;
	
	@ManyToMany(mappedBy="peliculas",fetch=FetchType.LAZY,cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	@JsonIgnoreProperties({"hibernateLazyInitializer","peliculas"})
	private Set<Personaje> personajes;
	
	@ManyToOne(optional = true, fetch=FetchType.LAZY,cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="id_genero",foreignKey=@ForeignKey(name="fk_genero"))
	@JsonIgnoreProperties({"hibernateLazyInitializer","peliculas"})
	private Genero genero;
	
	
	public Pelicula(Integer id, String imagen, String titulo, LocalDateTime fechaCreacion, Integer calificacion,
			Genero genero) {
		super();
		this.id = id;
		this.imagen = imagen;
		this.titulo = titulo;
		this.fechaCreacion = fechaCreacion;
		this.calificacion = calificacion;
		this.genero = genero;
	}
	
	
	public Pelicula(Integer id, String imagen, String titulo, LocalDateTime fechaCreacion, Integer calificacion,
			Genero genero,Set<Personaje> personajes) {
		this(id,imagen,titulo,fechaCreacion,calificacion,genero);
		this.personajes = personajes;
	}
	
	
	@PrePersist
	public void antesPersistir() {
		this.fechaCreacion = LocalDateTime.now();
	}
}
