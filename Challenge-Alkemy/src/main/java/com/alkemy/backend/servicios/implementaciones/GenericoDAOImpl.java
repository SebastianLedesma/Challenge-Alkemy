package com.alkemy.backend.servicios.implementaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.alkemy.backend.servicios.contratos.IGenericoDAO;

public class GenericoDAOImpl<E,R extends CrudRepository<E,Integer>> implements IGenericoDAO<E> {

	protected R repository;
	
	public GenericoDAOImpl(R repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly=true)
	public List<E> findAll() {
		return (List<E>) this.repository.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<E> findById(Integer id) {
		return this.repository.findById(id);
	}

	@Override
	@Transactional
	public E save(E entity) {
		return this.repository.save(entity);
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		this.repository.deleteById(id);
	}

}
