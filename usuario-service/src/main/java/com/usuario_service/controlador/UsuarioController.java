package com.usuario_service.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/api/auth/success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        // Obtener el correo y otros datos del usuario autenticado
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");

        // Aquí puedes generar un token JWT para el usuario si es necesario
        String jwtToken = "GenerarTokenAqui";  // Genera tu token JWT

        // Devolver una respuesta JSON con el token o mensaje de éxito
        return "Autenticación exitosa! Token JWT: " + jwtToken;
    }
	
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
	
	@CircuitBreaker(name = "carrosCB", fallbackMethod = "fallBackGetCarros")
	@GetMapping("/carros/{usuarioId}")
	public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int id){
		Usuario usuario = usuarioService.getUsuarioById(id);
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<Carro> carros = usuarioService.getCarros(id);
		return ResponseEntity.ok(carros);
	}
	
	@CircuitBreaker(name = "motosCB",fallbackMethod = "fallBackGetMotos")
	@GetMapping("/motos/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int id){
		Usuario usuario = usuarioService.getUsuarioById(id);
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<Moto> motos = usuarioService.getMotos(id);
		return ResponseEntity.ok(motos);
	}
	
	@CircuitBreaker(name = "carrosCB",fallbackMethod = "fallBackSaveCarro")
	@PostMapping("/carro/{usuarioId}")
	public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId") int usuarioId,@RequestBody Carro carro){
		Carro nuevoCarro = usuarioService.saveCarro(usuarioId, carro);
		return ResponseEntity.ok(nuevoCarro);
	}

	@CircuitBreaker(name = "motosCB",fallbackMethod = "fallBackSaveMoto")
	@PostMapping("/moto/{usuarioId}")
	public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId,@RequestBody Moto moto){
		Moto nuevaMoto = usuarioService.saveMoto(usuarioId, moto);
		return ResponseEntity.ok(nuevaMoto);
	}
	
	@CircuitBreaker(name = "todosCB",fallbackMethod = "fallBackGetTodos")
	@GetMapping("/todos/{usuarioId}")
	public ResponseEntity<Map<String, Object>> listarTodosLosVehiculos(@PathVariable("usuarioId") int usuarioId){
		Map<String,Object> resultado = usuarioService.getUsuarioAndVehiculos(usuarioId);
		return ResponseEntity.ok(resultado);
	}
	
	@CircuitBreaker(name = "carrosCB", fallbackMethod = "fallBackUpdateCarro")
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
	
	@CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackUpdateMoto")
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
	
	@CircuitBreaker(name = "carrosCB", fallbackMethod = "fallBackDeleteCarro")
	@DeleteMapping("/carro/{carroId}")
	public ResponseEntity<Void> eliminarCarro(@PathVariable int carroId){
		try {
			usuarioService.deleteCarro(carroId);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackDeleteMoto")
	@DeleteMapping("/moto/{motoId}")
	public ResponseEntity<Void> eliminarMoto(@PathVariable int motoId){
		try {
			usuarioService.deleteMoto(motoId);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	private ResponseEntity<List<Carro>> fallBackGetCarros(@PathVariable("usuarioId") int id,
			RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede visualizar los carros en este momento. Por favor, intenta más tarde.",
				HttpStatus.OK);
	}

	private ResponseEntity<Carro> fallBackSaveCarro(@PathVariable("usuarioId") int id, @RequestBody Carro carro,
			RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede guardar los carros en este momento. Por favor, intenta más tarde.",
				HttpStatus.OK);
	}

	private ResponseEntity<Carro> fallBackUpdateCarro(@PathVariable("usuarioId") int id,
			@RequestBody Carro carroActualizado, RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede actualizar en este momento. Por favor, intenta más tarde.",
				HttpStatus.OK);
	}

	private ResponseEntity<Void> fallBackDeleteCarro(@PathVariable("usuarioId") int id, RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede eliminar en este momento. Por favor, intenta más tarde.",
				HttpStatus.OK);
	}
	

	private ResponseEntity<List<Moto>> fallBackGetMotos(@PathVariable("usuarioId") int id, RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede visualizar las motos en este momento. Por favor, intenta más tarde.",
				HttpStatus.OK);
	}

	private ResponseEntity<Moto> fallBackSaveMoto(@PathVariable("usuarioId") int id, @RequestBody Moto moto,
			RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede guardar las motos en este momento. Por favor, intenta mas tarde.",
				HttpStatus.OK);
	}
	
	private ResponseEntity<Moto> fallBackUpdateMoto(@PathVariable("usuarioId") int id,
			@RequestBody Moto motoActualizado, RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede actualizar en este momento. Por favor, intenta más tarde.",
				HttpStatus.OK);
	}
	
	private ResponseEntity<Void> fallBackDeleteMoto(@PathVariable("usuarioId") int id, RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id + " no puede eliminar en este momento. Por favor, intenta más tarde.",
				HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> fallBackGetTodos(@PathVariable("usuarioId") int id,
			RuntimeException exception) {
		return new ResponseEntity(
				"El usuario : " + id
						+ " no puede ver todos los vehiculos en este momento. Por favor, intenta mas tarde.",
				HttpStatus.OK);
	}
	
}
