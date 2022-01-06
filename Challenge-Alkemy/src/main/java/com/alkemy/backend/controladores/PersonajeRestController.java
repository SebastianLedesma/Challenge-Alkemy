package com.alkemy.backend.controladores;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alkemy.backend.modelo.dto.PersonajeDTO;
import com.alkemy.backend.modelo.entidades.Pelicula;
import com.alkemy.backend.modelo.entidades.Personaje;
import com.alkemy.backend.modelo.mapper.IPersonajeMapper;
import com.alkemy.backend.servicios.contratos.IPeliculaDAO;
import com.alkemy.backend.servicios.contratos.IPersonajeDAO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path="/characters")
@Api(tags="AMB de personajes.")
public class PersonajeRestController {

	@Autowired
	private IPersonajeDAO personajeDAO;
	
	@Autowired
	private IPeliculaDAO peliculaDAO;
	
	@Autowired
	private IPersonajeMapper personajeMapper;
	
	
	@GetMapping
	@ApiOperation("Obtener todos los personajes")
	@ApiResponse(code=200,message="OK")
	public ResponseEntity<?> obtenerTodos(){
		Map<String,Object> response = new HashMap<>();
		List<Personaje> personajes = this.personajeDAO.findAll();
		
		if(personajes.isEmpty()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", "No hay personajes disponibles.");
			return ResponseEntity.badRequest().body(response);
		}
		
		List<PersonajeDTO> personajesDTO = personajes
											.stream()
											.map(pers -> this.personajeMapper.mapPersonaje(pers))
											.collect(Collectors.toList());
		
		response.put("success", Boolean.TRUE);
		response.put("datos",personajesDTO);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/{id}")
	@ApiOperation("Buscar un personaje por id.")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=404,message="Personaje no encontrado.")
	})
	public ResponseEntity<?> obtenerPorId(
								@ApiParam(value="Id del personaje",
								required=true,
								example="8") @PathVariable Integer id){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Personaje> optPersonaje= this.personajeDAO.findById(id);
		
		if(!optPersonaje.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el personaje con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}

		response.put("success", Boolean.TRUE);
		response.put("datos",optPersonaje.get());
		return ResponseEntity.ok(response);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="Personaje creado."),
		@ApiResponse(code=400,message="Error.Verifique los datos enviados.")
	})
	@PostMapping
	public ResponseEntity<?> altaPersonaje(@Valid @ApiParam(value="Personaje a enviar") @RequestBody Personaje personaje,BindingResult result){
		Map<String,Object> response = new HashMap<>();
		Map<String,Object> validaciones = new HashMap<>();
		
		if(result.hasErrors()) {
			result.getFieldErrors().forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));
			response.put("success", Boolean.FALSE);
			response.put("mensaje", validaciones);
			return ResponseEntity.badRequest().body(response);
		}
				
		response.put("success", Boolean.TRUE);
		response.put("datos",this.personajeDAO.save(personaje));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="Personaje modificado."),
		@ApiResponse(code=400,message="Error.Verifique los datos enviados."),
		@ApiResponse(code=404,message="No se encontró el personaje.")
	})
	@PutMapping("/{id}")
	public ResponseEntity<?> actualializaPersonaje(@ApiParam(value="Id del personaje",required=true,example="8") 
												@PathVariable Integer id,@ApiParam(value="Personaje con datos modificados")
												@Valid @RequestBody Personaje personaje,BindingResult result){
		
		Map<String,Object> response = new HashMap<>();
		Map<String,Object> validaciones = new HashMap<>();
		Optional<Personaje> optPersonaje  = this.personajeDAO.findById(id);
		if(!optPersonaje.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el personaje con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		if(result.hasErrors()) {
			result.getFieldErrors().forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));
			response.put("success", Boolean.FALSE);
			response.put("mensaje", validaciones);
			return ResponseEntity.badRequest().body(response);
		}
		
		Personaje personajeUpdate = optPersonaje.get();
		
		personajeUpdate.setEdad(personaje.getEdad());
		personajeUpdate.setHistoria(personaje.getHistoria());
		personajeUpdate.setNombre(personaje.getNombre());
		personajeUpdate.setPeso(personaje.getPeso());
		
		response.put("success", Boolean.TRUE);
		response.put("datos",this.personajeDAO.save(personajeUpdate));
		return ResponseEntity.ok(response);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="Pelicula asignada."),
		@ApiResponse(code=404,message="Error.Verifique los datos enviados.")
	})
	@PutMapping("/{idPersonaje}/{idPelicula}")
	public  ResponseEntity<?> asignarPelicula(@ApiParam(value="Id del personaje",required=true,example="2")
												@PathVariable Integer idPersonaje,
												@ApiParam(value="Id de la pelicula",required=true,example="6") 
												@PathVariable Integer idPelicula){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Personaje> optPersonaje  = this.personajeDAO.findById(idPersonaje);
		Optional<Pelicula> optPelicula= this.peliculaDAO.findById(idPelicula);
		Personaje personaje = null;
		
		if(!optPersonaje.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el personaje con ID %d", idPersonaje));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		if(!optPelicula.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró la película con ID %d", idPelicula));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		personaje = optPersonaje.get();
		personaje.getPeliculas().add(optPelicula.get());
		optPelicula.get().getPersonajes().add(personaje);
		
		response.put("success", Boolean.TRUE);
		response.put("datos",this.personajeDAO.save(personaje));
		return ResponseEntity.ok(response);

	}
	
	
	@ApiOperation(value="Subir una imagen para el personaje",consumes="multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="file",paramType="form",required=true,dataType="file")
	})
	@PostMapping("/{id}")
	public ResponseEntity<?> asignarImagen(@ApiParam(value="file",required=true) @RequestPart("file") MultipartFile file,
									@ApiParam(value="Id del personaje",required=true,example="2") @PathVariable Integer id){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Personaje> optPersonaje  = this.personajeDAO.findById(id);
		Personaje personaje = null;
		
		if(!optPersonaje.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el personaje con ID %d", id));
			return ResponseEntity.badRequest().body(response);
		}
		
		if(!file.isEmpty()) {
			personaje = optPersonaje.get();
			String nombreFotoAnterior = personaje.getImagen();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads/personajes").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			String nombreArch= id.toString() + "_" + personaje.getNombre()+
					file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
			
			Path rutaArchivo= Paths.get("uploads").resolve(nombreArch).toAbsolutePath();
			
			try {
				Files.copy(file.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("success", Boolean.FALSE);
				response.put("mensaje", "Error al subir la imagen.");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			
			personaje.setImagen(nombreArch);
			
			response.put("success", Boolean.TRUE);
			response.put("personaje",this.personajeMapper.mapPersonaje(this.personajeDAO.save(personaje)));
			response.put("mensaje","Se ha subido la imagen correctamente.");
			return ResponseEntity.ok(response);
		}else {
			response.put("success", Boolean.FALSE);
			response.put("mensaje", "Debe enviar una imagen.");
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
	@ApiResponses({
		@ApiResponse(code=204,message="Personaje eliminado."),
		@ApiResponse(code=404,message="Error.Verifique los datos enviados."),
		@ApiResponse(code=500,message="Error de servidor.")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarPersonaje(@ApiParam(value="Personaje a eliminar",required=true,example="3") 
											@PathVariable Integer id){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Personaje> optPersonaje  = this.personajeDAO.findById(id);
		if(!optPersonaje.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el personaje con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			String nombreFotoAnterior = optPersonaje.get().getImagen();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads/personajes").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
		}catch(DataAccessException ex) {
			response.put("mensaje", "Error al eliminar en la base de datos.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		this.personajeDAO.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="Personajes filtrados."),
		@ApiResponse(code=404,message="Error.Verifique los datos enviados.")
	})
	@GetMapping("/GET/characters")
	public ResponseEntity<?> filtrarPersonajes(
								@RequestParam(name="name",defaultValue="") String name,
								@RequestParam(name="age",defaultValue="0") Integer age,
								@RequestParam(name="weight",defaultValue="0.0") Double weight,
								@RequestParam(name="idMovie",defaultValue="0") Integer idMovie){
		
		Map<String,Object> response = new HashMap<>();
		boolean personajeEncontrado= false;
		if(name.length() > 0) {
			Personaje personajeNombre = this.personajeDAO.findByNombre(name);
			if(personajeNombre == null) {
				response.put("mensaje", String.format("No se encontraron personajes con el nombre %s", name));
			}else {
				personajeEncontrado=true;
				response.put("resultado", personajeNombre);
			}
			
		}else if(age != 0){
			List<Personaje> personajes = this.personajeDAO.findByEdad(age);
			if(personajes.isEmpty()) {
				response.put("mensaje", String.format("No se encontraron personajes con la edad %d", age));
			}else {
				personajeEncontrado=true;
				response.put("resultado", personajes);
			}
			
		}else if(idMovie != 0) {
			Optional<Pelicula> optPelicula = this.peliculaDAO.findById(idMovie);
			
			if(!optPelicula.isPresent()) {
				response.put("mensaje", String.format("No se encontró la película con ID %d", idMovie));
			}else {
				List<Personaje> personajes = this.personajeDAO.findByPeliculasId(idMovie);
				if(personajes.isEmpty()) {
					response.put("mensaje", String.format("No se encontraron personajes para la película con id %d", idMovie));
				}else {
					response.put("resultado", personajes);
					personajeEncontrado=true;
				}
			}
			
		}else if(weight > 0) {
			List<Personaje> personajes = this.personajeDAO.findByPeso(weight);
			if(personajes.isEmpty()) {
				response.put("mensaje", String.format("No se encontraron personajes que pesen %.2f KG.", weight));
			}else {
				response.put("resultado",personajes);
				personajeEncontrado=true;
			}
		}
		
		if(!personajeEncontrado) {
			response.put("success", Boolean.FALSE);
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}else {
			response.put("success", Boolean.TRUE);
			return ResponseEntity.ok(response);
		}
		
		
	}
	
	
	
	
}
