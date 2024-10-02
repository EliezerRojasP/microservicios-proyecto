package com.usuario_service.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.carro_service.entidades.Carro;
import com.usuario_service.entidades.Usuario;
import com.usuario_service.feignclients.CarroFeignClient;
import com.usuario_service.feignclients.MotoFeignClient;
import com.usuario_service.modelos.Moto;
import com.usuario_service.repositorio.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private RestTemplate restTemplate;
		
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CarroFeignClient carroFeingClient;
	
	@Autowired
	private MotoFeignClient motoFeignClient;
	
	
	public List<Usuario> getAll() {
		return usuarioRepository.findAll();
	}

	public Usuario getUsuarioById(int id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	public Usuario save(Usuario usuario) {
		Usuario nuevoUsuario = usuarioRepository.save(usuario);
		return nuevoUsuario;
	}

	public List<Carro> getCarros(int usuarioId) {
		List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/" + usuarioId, List.class);
		return carros;
	}

	public List<Moto> getMotos(int usuarioId) {
		List<Moto> motos = restTemplate.getForObject("http://localhost:8003/moto/usuario/" + usuarioId, List.class);
		return motos;
	}

	public Carro saveCarro(int usuarioId, Carro carro) {
		carro.setUsuarioId(usuarioId);
		Carro nuevoCarro = carroFeingClient.save(carro);
		return nuevoCarro;
	}
	
	public Moto saveMoto(int usuarioId, Moto moto) {
		moto.setUsuarioId(usuarioId);
		Moto nuevaMoto = motoFeignClient.save(moto);
		return nuevaMoto;
	}
	
	public Map<String, Object> getUsuarioAndVehiculos(int usuarioId) {
		Map<String, Object> resultado = new HashMap<>();
		Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

		if (usuario == null) {
			resultado.put("Mensaje", "El usuario no existe");
			return resultado;
		}

		resultado.put("Usuario", usuario);

		// Verificación nula para carros
		List<Carro> carros = carroFeingClient.getCarros(usuarioId);
		if (carros == null || carros.isEmpty()) {
			resultado.put("Carros", "El usuario no tiene carros");
		} else {
			resultado.put("Carros", carros);
		}

		// Verificación nula para motos
		List<Moto> motos = motoFeignClient.getMotos(usuarioId);
		if (motos == null || motos.isEmpty()) {
			resultado.put("Motos", "El usuario no tiene motos");
		} else {
			resultado.put("Motos", motos);
		}

		return resultado;
	}

	
}
