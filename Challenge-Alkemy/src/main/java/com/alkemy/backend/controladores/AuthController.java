package com.alkemy.backend.controladores;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.backend.security.JWTUtil;
import com.alkemy.backend.security.dto.AuthenticationRequest;
import com.alkemy.backend.security.dto.AuthenticationResponse;
import com.alkemy.backend.security.entidades.Usuario;
import com.alkemy.backend.security.sendgrid.MailService;
import com.alkemy.backend.security.servicios.UserDetailServiceImpl;
import com.alkemy.backend.security.servicios.UsuarioDAO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(path="/auth")
@Api(tags="Crear o ingresar a la API.")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailServiceImpl userDetails;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@ApiOperation("Obtener todos los personajes")
	@ApiResponses({
		@ApiResponse(code=201,message="Usuario registrado correctamente"),
		@ApiResponse(code=400,message="Error.Pruebe con otros datos.")
	})
	@PostMapping("/register")
	public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario nuevoUsuario,BindingResult result){
		Map<String,Object> response = new HashMap<>();
		Map<String,Object> validaciones = new HashMap<>();
		
		if(result.hasErrors()) {
			result.getFieldErrors().forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));
			response.put("success", Boolean.FALSE);
			response.put("mensaje", validaciones);
			return ResponseEntity.badRequest().body(response);
		}
		
		if(this.usuarioDAO.existByNombreUsuario(nuevoUsuario.getNombreUsuario())) {
			response.put("errors","Ya existe un usuario con ese nombre.");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(this.usuarioDAO.existsByEmail(nuevoUsuario.getEmail())) {
			response.put("errors","Ya existe un usuario con ese email.");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
			
		this.usuarioDAO.save(nuevoUsuario);
		this.mailService.sendTextEmail(nuevoUsuario.getEmail());
		
		response.put("mensaje", "Usuario guardado.Verifique si email.");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=403,message="Error.Verifique los datos enviados.")
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
		Map<String,Object> response = new HashMap<>();
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
			UserDetails userDetails = this.userDetails.loadUserByUsername(request.getUserName());
			String jwt =jwtUtil.generateToken(userDetails);
			
			return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(jwt));
		}catch(BadCredentialsException e) {
			response.put("error", "Si ya se registró verifique su username y password.");
			return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
		}
	}
}
