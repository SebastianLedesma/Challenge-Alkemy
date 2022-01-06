package com.alkemy.backend.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.backend.modelo.entidades.Pelicula;
import com.alkemy.backend.repositorios.IPeliculaRepository;
import com.alkemy.backend.servicios.contratos.IPeliculaDAO;

@Service
public class PeliculaDAOImpl extends GenericoDAOImpl<Pelicula, IPeliculaRepository> implements IPeliculaDAO{

	@Autowired
	public PeliculaDAOImpl(IPeliculaRepository repository) {
		super(repository);
	}

	@Override
	public Pelicula buscarPorNombre(String titulo) {
		return this.repository.findByTitulo(titulo);
	}

	@Override
	public List<Pelicula> buscarPorGenero(Integer idGenero) {
		return this.repository.findByGeneroId(idGenero);
	}
	
	public List<Pelicula> ordenarPeliculas(String orden){
		if(orden.equals("DESC")) {
			return this.repository.OrderByFechaCreacionDesc();
		}else {
			return this.repository.OrderByFechaCreacionAsc();
		}
	}

}
