package com.alkemy.backend.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.backend.modelo.entidades.Personaje;

@Repository
public interface IPersonajeRepository extends CrudRepository<Personaje, Integer>{

	Personaje findByNombre(String nombre);
	
	List<Personaje> findByEdad(Integer edad);
	
	List<Personaje> findByPeliculasId(Integer idPelicula);
	
	List<Personaje> findByPeso(Double peso);
}
