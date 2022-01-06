package com.alkemy.backend.servicios.implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.backend.modelo.entidades.Genero;
import com.alkemy.backend.repositorios.IGeneroRepository;
import com.alkemy.backend.servicios.contratos.IGeneroDAO;

@Service
public class GeneroDAOImpl extends GenericoDAOImpl<Genero,IGeneroRepository> implements IGeneroDAO{

	@Autowired
	public GeneroDAOImpl(IGeneroRepository repository) {
		super(repository);
	}

	
}
