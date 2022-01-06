package com.alkemy.backend.servicios.contratos;

import java.util.List;
import java.util.Optional;

public interface IGenericoDAO<E> {

	List<E> findAll();
	
	Optional<E> findById(Integer id);
	
	E save(E entity);
	
	void deleteById(Integer id);
}
