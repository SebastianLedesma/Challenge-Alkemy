package com.alkemy.backend.security.servicios;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alkemy.backend.security.entidades.Usuario;


@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioDAO usuariosDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuariosDAO.getByNombreUsuario(username).get();
		return new User(usuario.getNombreUsuario(),"{noop}" + usuario.getPassword(),new ArrayList<>());
	}

}
