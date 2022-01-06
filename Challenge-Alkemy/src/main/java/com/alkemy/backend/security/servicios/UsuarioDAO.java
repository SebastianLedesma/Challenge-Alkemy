package com.alkemy.backend.security.servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alkemy.backend.security.entidades.Usuario;
import com.alkemy.backend.security.repositorios.IUsuarioRepository;

@Service
public class UsuarioDAO {

	@Autowired
	private IUsuarioRepository usuarioRepository;
	
	
	public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
		return this.usuarioRepository.findByNombreUsuario(nombreUsuario);
	}
	
	@Transactional(readOnly=true)
	public boolean existByNombreUsuario(String nombreUsuario) {
		return this.usuarioRepository.existsByNombreUsuario(nombreUsuario);
	}
	
	@Transactional(readOnly=true)
	public boolean existsByEmail(String email) {
		return this.usuarioRepository.existsByEmail(email);
	}
	
	@Transactional
	public void save(Usuario usuario) {
		this.usuarioRepository.save(usuario);
	}
}
