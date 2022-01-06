package com.alkemy.backend.servicios.contratos;

import java.util.List;


import com.alkemy.backend.modelo.entidades.Personaje;

public interface IPersonajeDAO extends IGenericoDAO<Personaje>{

	Personaje findByNombre(String nombre);
	
	List<Personaje> findByEdad(Integer edad);
	
	List<Personaje> findByPeliculasId(Integer idPelicula);
	
	List<Personaje> findByPeso(Double peso);
	
}
