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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alkemy.backend.modelo.entidades.Genero;
import com.alkemy.backend.servicios.contratos.IGeneroDAO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path="/genre")
@Api(tags="ABM de géneros de péliculas.")
public class GeneroRestController {

	@Autowired
	private IGeneroDAO generoDAO;
	
	
	@GetMapping
	public ResponseEntity<?> obtenerTodos(){
		Map<String,Object> response = new HashMap<>();
		List<Genero> personajes = this.generoDAO.findAll();
		
		if(personajes.isEmpty()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", "No hay generos disponibles.");
			return ResponseEntity.badRequest().body(response);
		}
		
		response.put("success", Boolean.TRUE);
		response.put("datos",personajes);
		return ResponseEntity.ok(response);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="Género encontrado."),
		@ApiResponse(code=404,message="Error.Verifique los datos enviados.")
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable Integer id){
		Map<String,Object> response = new HashMap<>();
		Optional<Genero> optGenero= this.generoDAO.findById(id);
		
		if(!optGenero.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el género con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}

		response.put("success", Boolean.TRUE);
		response.put("datos",optGenero.get());
		return ResponseEntity.ok(response);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=201,message="Género creado correctamente."),
		@ApiResponse(code=400,message="Error.Verifique los datos enviados.")
	})
	@PostMapping
	public ResponseEntity<?> altaGenro(@Valid @RequestBody Genero genero,BindingResult result){
		Map<String,Object> response = new HashMap<>();		
		Map<String,Object> validaciones = new HashMap<>();
		
		if(result.hasErrors()) {
			result.getFieldErrors().forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));
			response.put("success", Boolean.FALSE);
			response.put("mensaje", validaciones);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.put("success", Boolean.TRUE);
		response.put("datos",this.generoDAO.save(genero));
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=200,message="Género actualizado correctamente."),
		@ApiResponse(code=400,message="Error.Verifique los datos enviados.")
	})
	@PutMapping("/{id}")
	public ResponseEntity<?> actualiazarGenero(@PathVariable Integer id,@RequestBody Genero genero){
		Map<String,Object> response = new HashMap<>();
		Optional<Genero> optGenero  = this.generoDAO.findById(id);
		if(!optGenero.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el género con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		Genero generoUpdate = optGenero.get();
		generoUpdate.setNombre(genero.getNombre());
		
		response.put("success", Boolean.TRUE);
		response.put("datos",this.generoDAO.save(generoUpdate));
		return ResponseEntity.ok(response);
	}
	
	
	@ApiResponses({
		@ApiResponse(code=204,message="Género eliminado correctamente."),
		@ApiResponse(code=404,message="Error.Verifique los datos enviados.")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarGenero(@PathVariable Integer id){
		Map<String,Object> response = new HashMap<>();
		Optional<Genero> optGenero  = this.generoDAO.findById(id);
		if(!optGenero.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el género con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			String nombreFotoAnterior = optGenero.get().getImagen();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads/generos").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
		}catch(DataAccessException ex) {
			response.put("mensaje", "Error al eliminar en la base de datos.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		this.generoDAO.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@ApiOperation(value="Subir una imagen para el género",consumes="multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="file",paramType="form",required=true,dataType="file")
	})
	@PostMapping("/{id}")
	public ResponseEntity<?> asignarImagen(@ApiParam(value="file",required=true) @RequestPart("file") MultipartFile file,
									@ApiParam(value="Id del género",required=true,example="2") @PathVariable Integer id){
		
		Map<String,Object> response = new HashMap<>();
		Optional<Genero> optGenero  = this.generoDAO.findById(id);
		Genero genero = null;
		
		if(!optGenero.isPresent()) {
			response.put("success",Boolean.FALSE);
			response.put("mensaje", String.format("No se encontró el personaje con ID %d", id));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		if(!file.isEmpty()) {
			genero = optGenero.get();
			String nombreFotoAnterior = genero.getImagen();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads/generos").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			String nombreArch= id.toString() + "_" + genero.getNombre()+
					file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
			
			Path rutaArchivo= Paths.get("uploads/generos").resolve(nombreArch).toAbsolutePath();
			
			try {
				Files.copy(file.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("success", Boolean.FALSE);
				response.put("mensaje", "Error al subir la imagen.");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			
			genero.setImagen(nombreArch);
			
			response.put("success", Boolean.TRUE);
			response.put("genero",this.generoDAO.save(genero));
			response.put("mensaje","Se ha subido la imagen correctamente.");
			return ResponseEntity.ok(response);
		}else {
			response.put("success", Boolean.FALSE);
			response.put("mensaje", "Debe enviar una imagen.");
			return ResponseEntity.badRequest().body(response);
		}
	}
	
}
