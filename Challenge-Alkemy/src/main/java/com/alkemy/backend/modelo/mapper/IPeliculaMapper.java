package com.alkemy.backend.modelo.mapper;

import org.mapstruct.Mapper;

import com.alkemy.backend.modelo.dto.PeliculaDTO;
import com.alkemy.backend.modelo.entidades.Pelicula;

@Mapper(componentModel="spring")
public interface IPeliculaMapper {

	PeliculaDTO mapPelicula(Pelicula pelicula);
	
	Pelicula mapPelicula(PeliculaDTO peliculaDTO);
}
