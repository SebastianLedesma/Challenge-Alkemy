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

import com.alkemy.backend.modelo.dto.PeliculaDTO;
import com.alkemy.backend.modelo.entidades.Genero;
import com.alkemy.backend.modelo.entidades.Pelicula;
import com.alkemy.backend.modelo.mapper.IPeliculaMapper;
import com.alkemy.backend.servicios.contratos.IGeneroDAO;
import com.alkemy.backend.servicios.contratos.IPeliculaDAO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path="/movies")
@Api(tags="ABM de películas.")
public class PeliculaRestController {

	@Autowired
	private IPeliculaDAO peliculaDAO;
	
	@Autowired
	private IGeneroDAO generoDAO;
	
	@Autowired
	private IPeliculaMapper peliculaMapper;
	
	
	@ApiOperation("Obtener todos las películas")
	@GetMapping
	public ResponseEntity<?> obtenerTodos(){
		Map<String,Object> response = new HashMap<>();
		List<Pelicula> peliculas = this.peliculaDAO.findAll();
		
		if(peliculas.isEmpty()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", "No hay películas disponibles.");
			return ResponseEntity.badRequest().body(response);
		}
		List<PeliculaDTO> peliculasDTO = peliculas
											.stream()
											.map(movie -> this.peliculaMapper.mapPelicula(movie))
											.collect(Collectors.toList());
		
		response.put("success", Boolean.TRUE);
		response.put("datos",peliculasDTO);
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation("Obtener personaje por id")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=404,message="Peliculas no encontrada.")
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@ApiParam(value="Id de la pelicula",required=true,example="8") 
											@PathVariable Integer id){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Pelicula> optPelicula= this.peliculaDAO.findById(id);
		
		if(!optPelicula.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró la película con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}

		response.put("success", Boolean.TRUE);
		response.put("datos",optPelicula.get());
		return ResponseEntity.ok(response);
	}
	
	
	@ApiOperation("Alta de una película")
	@ApiResponses({
		@ApiResponse(code=201,message="Película creada."),
		@ApiResponse(code=400,message="Error.Verifique los datos.")
	})
	@PostMapping
	public ResponseEntity<?> altaPelicula(@Valid @RequestBody @ApiParam(value="Película a enviar:") Pelicula pelicula,BindingResult result){
		Map<String,Object> response = new HashMap<>();
		Map<String,Object> validaciones = new HashMap<>();
		
		if(result.hasErrors()) {
			result.getFieldErrors().forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));
			response.put("success", Boolean.FALSE);
			response.put("mensaje", validaciones);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.put("success", Boolean.TRUE);
		response.put("datos",this.peliculaDAO.save(pelicula));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	@PutMapping("/{id}")
	@ApiResponses({
		@ApiResponse(code=200,message="Película actualizada."),
		@ApiResponse(code=404,message="Error.Verifique los datos enviados.")
	})
	public ResponseEntity<?> actualizarPelicula(@ApiParam(value="Id de la pelicula:",required=true)
												@PathVariable Integer id,
												@Valid @RequestBody @ApiParam(value="Película con datos modificados:") Pelicula pelicula,
												BindingResult result){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Pelicula> optPelicula = this.peliculaDAO.findById(id);
		Map<String,Object> validaciones = new HashMap<>();
		
		if(!optPelicula.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró la película con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		if(result.hasErrors()) {
			result.getFieldErrors().forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));
			response.put("success", Boolean.FALSE);
			response.put("mensaje", validaciones);
			return ResponseEntity.badRequest().body(response);
		}
		
		Pelicula peliculaUpdate = optPelicula.get();
		peliculaUpdate.setTitulo(pelicula.getTitulo());
		peliculaUpdate.setCalificacion(pelicula.getCalificacion());
		
		response.put("success", Boolean.TRUE);
		response.put("resultado", this.peliculaDAO.save(peliculaUpdate));
		return ResponseEntity.ok(response);
	}
	
	
	
	@ApiOperation(value="Subir una imagen para la película",consumes="multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="file",paramType="form",required=true,dataType="file")
	})
	@PostMapping("/{id}")
	public ResponseEntity<?> asignarImagen(@ApiParam(value="file",required=true) @RequestPart("file") MultipartFile file,
										@ApiParam(value="Id de la película",required=true,example="2") @PathVariable Integer id){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Pelicula> optPelicula  = this.peliculaDAO.findById(id);
		Pelicula pelicula = null;
		
		if(!optPelicula.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró la película con ID %d", id));
			return ResponseEntity.badRequest().body(response);
		}
		
		if(!file.isEmpty()) {
			pelicula = optPelicula.get();
			String nombreFotoAnterior = pelicula.getImagen();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads/peliculas").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			String nombreArch= id.toString() + "_" + pelicula.getTitulo()+
					file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
			
			Path rutaArchivo= Paths.get("uploads/peliculas").resolve(nombreArch).toAbsolutePath();
			
			try {
				Files.copy(file.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("success", Boolean.FALSE);
				response.put("mensaje", "Error al subir la imagen.");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			
			pelicula.setImagen(nombreArch);
			
			response.put("success", Boolean.TRUE);
			response.put("personaje",this.peliculaDAO.save(pelicula));
			response.put("mensaje","Se ha subido la imagen correctamente.");
			return ResponseEntity.ok(response);
		}else {
			response.put("success", Boolean.FALSE);
			response.put("mensaje", "Debe enviar una imagen.");
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
	@ApiResponses({
		@ApiResponse(code=204,message="Género asignado."),
		@ApiResponse(code=400,message="Error.Verifique los datos enviados.")
	})
	@PutMapping("/{idPelicula}/{idGenero}")
	public ResponseEntity<?> asignarGenero(@ApiParam(value="Id de pelicula",required=true,example="3") 
											@PathVariable Integer idPelicula,
											@ApiParam(value="Id del género",required=true,example="1")
											@PathVariable Integer idGenero){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Pelicula> optPelicula= this.peliculaDAO.findById(idPelicula);
		Optional<Genero> optGenero= this.generoDAO.findById(idGenero);
		
		if(!optPelicula.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró la película con ID %d", idPelicula));
			return ResponseEntity.badRequest().body(response);
		}
		
		if(!optGenero.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el género con ID %d", idGenero));
			return ResponseEntity.badRequest().body(response);
		}
		
		Pelicula pelicula = optPelicula.get();
		pelicula.setGenero(optGenero.get());
		response.put("success", Boolean.TRUE);
		response.put("datos",this.peliculaDAO.save(pelicula));
		return ResponseEntity.ok(response);
		
	}
	
	
	@ApiResponses({
		@ApiResponse(code=204,message="Pelicula eliminada."),
		@ApiResponse(code=404,message="Pelicula no encontrada."),
		@ApiResponse(code=500,message="Error de servidor.")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarPelicula(@ApiParam(value="Pelicula a eliminar",required=true,example="3") 
												@PathVariable Integer id){
		Map<String,Object> response = new HashMap<>();
		
		Optional<Pelicula> optPelicula = this.peliculaDAO.findById(id);
		if(!optPelicula.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró la película con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			String nombreFotoAnterior = optPelicula.get().getImagen();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads/peliculas").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
		}catch(DataAccessException ex) {
			response.put("mensaje", "Error al eliminar en la base de datos.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		this.peliculaDAO.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="Datos encontrados."),
		@ApiResponse(code=404,message="Error.Verifique los datos enviados.")
	})
	@GetMapping("/movies")
	public ResponseEntity<?> filtrarPeliculas(@RequestParam(name="name",defaultValue="") String nombre,
											@RequestParam(name="genre",defaultValue="0") Integer idGenero,
											@RequestParam(name="order",defaultValue="") String order){
		
		Map<String,Object> response = new HashMap<>();
		boolean peliculaEncontrada = true;
		if(nombre.length() > 0) {
			Pelicula pelicula = this.peliculaDAO.buscarPorNombre(nombre);
			if(pelicula == null) {
				peliculaEncontrada=false;
				response.put("mensaje", String.format("No se encontró la película con el nombre %s", nombre));
			}else {
				response.put("resultado", pelicula);
			}
		}else if(idGenero != 0) {
			List<Pelicula> peliculas = this.peliculaDAO.buscarPorGenero(idGenero);
			if(peliculas.isEmpty()) {
				response.put("mensaje", String.format("No se encontraron películas de género con id %d", idGenero));
				peliculaEncontrada=false;
			}else {
				response.put("resultado", peliculas);
			}
		}else if(order.equals("DESC") || order.equals("ASC")){
			List<Pelicula> peliculas = this.peliculaDAO.ordenarPeliculas(order);
			response.put("resultado", peliculas);
		}else {
			peliculaEncontrada = false;
			response.put("message", "Los valores enviados son incorrectos.");
		}
		
		if(!peliculaEncontrada) {
			response.put("success", Boolean.FALSE);
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}else {
			response.put("success", Boolean.FALSE);
			return ResponseEntity.ok(response);
		}
	}
	
	
}
