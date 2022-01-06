package com.alkemy.backend.security.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alkemy.backend.security.entidades.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer>{

	Optional<Usuario> findByNombreUsuario(@Param("filtro") String nombreUsuario);
	
	boolean existsByNombreUsuario(String nombreUsuario);
	
	boolean existsByEmail(String email);
}
