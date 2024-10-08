package com.moto_service.controladores;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moto_service.entidades.Moto;
import com.moto_service.servicios.MotoService;

@RestController
@RequestMapping("/moto")
public class MotoController {
	
	@Autowired
	private MotoService motoService;
	
	@GetMapping
	public ResponseEntity<List<Moto>> listarMotos(){
		List<Moto> motos = motoService.getAll();
		if(motos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(motos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Moto> obtenerMoto(@PathVariable("id") int id){
		Moto moto = motoService.getMotoById(id);
		if(moto == null){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(moto);
	}
	
	@PostMapping
	public ResponseEntity<Moto> guardarMoto(@RequestBody Moto moto){
		Moto nuevaMoto = motoService.save(moto);
		return ResponseEntity.ok(nuevaMoto);
	}
	
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotosPorUsuarioId(@PathVariable("usuarioId") int id){
			List<Moto> motos = motoService.findByUsuarioId(id);
			if(motos.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(motos);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Moto> updateMoto(@PathVariable int id, @RequestBody Moto updatedMoto) {
		Moto moto = motoService.updateMoto(id, updatedMoto);
		if(moto != null) {
			return ResponseEntity.ok(moto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMoto(@PathVariable int id) {
		try {
			motoService.deleteMoto(id); // Si no existe, lanza una excepción
			return ResponseEntity.noContent().build(); // Retorna 204 No Content
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra la moto
		}
	}
}
