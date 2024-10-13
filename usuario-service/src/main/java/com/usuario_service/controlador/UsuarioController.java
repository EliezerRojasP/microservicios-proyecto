package com.usuario_service.controlador;

import java.util.List;
import java.util.Map;

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

import com.carro_service.entidades.Carro;
import com.usuario_service.entidades.Usuario;
import com.moto_service.entidades.Moto;
import com.usuario_service.servicio.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios(){
		List<Usuario> usuarios = usuarioService.getAll();
		if(usuarios.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(usuarios);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id){
		Usuario usuario = usuarioService.getUsuarioById(id);
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(usuario);
	}
	
	@PostMapping
	public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario){
		Usuario nuevoUsuario = usuarioService.save(usuario);
		return ResponseEntity.ok(nuevoUsuario);
	}
	
	@GetMapping("/carros/{usuarioId}")
	public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int id){
		Usuario usuario = usuarioService.getUsuarioById(id);
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<Carro> carros = usuarioService.getCarros(id);
		return ResponseEntity.ok(carros);
	}
	
	@GetMapping("/motos/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int id){
		Usuario usuario = usuarioService.getUsuarioById(id);
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<Moto> motos = usuarioService.getMotos(id);
		return ResponseEntity.ok(motos);
	}
	
	@PostMapping("/carro/{usuarioId}")
	public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId") int usuarioId,@RequestBody Carro carro){
		Carro nuevoCarro = usuarioService.saveCarro(usuarioId, carro);
		return ResponseEntity.ok(nuevoCarro);
	}
	
	
	@PostMapping("/moto/{usuarioId}")
	public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId,@RequestBody Moto moto){
		Moto nuevaMoto = usuarioService.saveMoto(usuarioId, moto);
		return ResponseEntity.ok(nuevaMoto);
	}
	
	@GetMapping("/todos/{usuarioId}")
	public ResponseEntity<Map<String, Object>> listarTodosLosVehiculos(@PathVariable("usuarioId") int usuarioId){
		Map<String,Object> resultado = usuarioService.getUsuarioAndVehiculos(usuarioId);
		return ResponseEntity.ok(resultado);
	}
	
	@PutMapping("/carro/{carroId}")
	public ResponseEntity<Carro> actualizarCarro(@PathVariable int carroId, @RequestBody Carro carroActualizado) {
		Carro carroExistente = usuarioService.getCarroById(carroId);
		if (carroExistente == null) {
			return ResponseEntity.notFound().build();
		}

		carroExistente.setMarca(carroActualizado.getMarca());
		carroExistente.setModelo(carroActualizado.getModelo());

		Carro carroActualizadoEnBD = usuarioService.saveCarro(carroExistente.getUsuarioId(), carroExistente);
		return ResponseEntity.ok(carroActualizadoEnBD);
	}
	
	@PutMapping("/moto/{motoId}")
	public ResponseEntity<Moto> actualizarMoto(@PathVariable int motoId, @RequestBody Moto motoActualizado){
		Moto motoExistente = usuarioService.getMotoById(motoId);
		if (motoExistente == null) {
			return ResponseEntity.notFound().build();
		}
		
		motoExistente.setMarca(motoActualizado.getMarca());
		motoExistente.setModelo(motoActualizado.getModelo());
		
		Moto motoActualizadoEnBD = usuarioService.saveMoto(motoExistente.getUsuarioId(), motoExistente);
		return ResponseEntity.ok(motoActualizadoEnBD);
		
	}
	
	@DeleteMapping("/carro/{carroId}")
	public ResponseEntity<Void> eliminarCarro(@PathVariable int carroId){
		try {
			usuarioService.deleteCarro(carroId);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/moto/{motoId}")
	public ResponseEntity<Void> eliminarMoto(@PathVariable int motoId){
		try {
			usuarioService.deleteMoto(motoId);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
