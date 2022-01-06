package com.alkemy.backend.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PeliculaDTO {

	private String titulo;
	private String imagen;
	private String fechaCreacion;
}
