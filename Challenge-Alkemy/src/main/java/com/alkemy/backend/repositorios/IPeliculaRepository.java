package com.alkemy.backend.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.backend.modelo.entidades.Pelicula;

@Repository
public interface IPeliculaRepository extends JpaRepository<Pelicula, Integer>{

	Pelicula findByTitulo(String titulo);
	
	List<Pelicula> findByGeneroId(Integer id);
	
	List<Pelicula> OrderByFechaCreacionDesc();
	
	List<Pelicula> OrderByFechaCreacionAsc();
}
