package com.alkemy.backend.servicios.contratos;

import java.util.List;

import com.alkemy.backend.modelo.entidades.Pelicula;

public interface IPeliculaDAO extends IGenericoDAO<Pelicula>{

	Pelicula buscarPorNombre(String nombre);
	
	List<Pelicula> buscarPorGenero(Integer idGenero);
	
	List<Pelicula> ordenarPeliculas(String order);
}
