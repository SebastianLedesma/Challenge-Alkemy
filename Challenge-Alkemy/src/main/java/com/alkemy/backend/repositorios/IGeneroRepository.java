package com.alkemy.backend.repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.backend.modelo.entidades.Genero;

@Repository
public interface IGeneroRepository extends CrudRepository<Genero, Integer>{

}
