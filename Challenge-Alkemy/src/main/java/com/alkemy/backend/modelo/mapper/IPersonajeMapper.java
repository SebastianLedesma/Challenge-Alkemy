package com.alkemy.backend.modelo.mapper;

import org.mapstruct.Mapper;

import com.alkemy.backend.modelo.dto.PersonajeDTO;
import com.alkemy.backend.modelo.entidades.Personaje;

@Mapper(componentModel="spring")
public interface IPersonajeMapper {

	PersonajeDTO mapPersonaje(Personaje personaje);
	
	Personaje mapPersonaje(PersonajeDTO personajeDTO);
}
