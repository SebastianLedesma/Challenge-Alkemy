package com.alkemy.backend.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alkemy.backend.modelo.entidades.Personaje;
import com.alkemy.backend.repositorios.IPersonajeRepository;
import com.alkemy.backend.servicios.contratos.IPersonajeDAO;

@Service
public class PersonajeDAOImpl extends GenericoDAOImpl<Personaje, IPersonajeRepository> implements IPersonajeDAO{

	@Autowired
	public PersonajeDAOImpl(IPersonajeRepository repository) {
		super(repository);
	}

	@Override
	@Transactional(readOnly=true)
	public Personaje findByNombre(String nombre) {
		return this.repository.findByNombre(nombre);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Personaje> findByEdad(Integer edad) {
		return this.repository.findByEdad(edad);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Personaje> findByPeliculasId(Integer idPelicula) {
		return this.repository.findByPeliculasId(idPelicula);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Personaje> findByPeso(Double peso) {
		return this.repository.findByPeso(peso);
	}

}
