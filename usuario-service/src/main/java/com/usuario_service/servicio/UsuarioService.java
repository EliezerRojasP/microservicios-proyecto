package com.usuario_service.servicio;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.carro_service.entidades.Carro;
import com.usuario_service.entidades.Usuario;
import com.usuario_service.feignclients.CarroFeignClient;
import com.usuario_service.feignclients.MotoFeignClient;
import com.moto_service.entidades.Moto;
import com.usuario_service.repositorio.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	
	public Carro getCarroById(int carroId) {
		return carroFeingClient.getCarroById(carroId);
	}
	
	public Moto getMotoById(int motoId) {
		return motoFeignClient.getMotoById(motoId);
	}

	public List<Carro> getCarros(int usuarioId) {
		List<Carro> carros = restTemplate.getForObject("http://carro-service/carro/usuario/" + usuarioId, List.class);
		return carros;
	}

	public List<Moto> getMotos(int usuarioId) {
		List<Moto> motos = restTemplate.getForObject("http://moto-service/moto/usuario/" + usuarioId, List.class);
		return motos;
	}

	public Carro saveCarro(int usuarioId, Carro carro) {
	    carro.setUsuarioId(usuarioId);  // Asigna el usuarioId

	    System.out.println("Enviando solicitud de guardado para carro con marca: " + carro.getMarca() + " y modelo: " + carro.getModelo());
	    
	    Carro nuevoCarro = carroFeingClient.save(carro);  // Llama al Feign Client para crear el carro

	    if(nuevoCarro != null) {
	        System.out.println("Carro guardado con éxito. ID del carro: " + nuevoCarro.getId());
	    } else {
	        System.out.println("Error al guardar el carro. La respuesta del microservicio es nula.");
	    }

	    return nuevoCarro;
	}

	public Moto saveMoto(int usuarioId, Moto moto) {
	    moto.setUsuarioId(usuarioId);  // Asigna el usuarioId

	    System.out.println("Enviando solicitud de guardado para moto con marca: " + moto.getMarca() + " y modelo: " + moto.getModelo());
	    
	    Moto nuevaMoto = motoFeignClient.save(moto);  // Llama al Feign Client para crear la moto

	    if(nuevaMoto != null) {
	        System.out.println("Moto guardada con éxito. ID de la moto: " + nuevaMoto.getId());
	    } else {
	        System.out.println("Error al guardar la moto. La respuesta del microservicio es nula.");
	    }

	    return nuevaMoto;
	}
	
	public void deleteCarro(int carroId){
		carroFeingClient.deleteCarro(carroId);
	}
	
	public void deleteMoto(int motoId) {
		motoFeignClient.deleteMoto(motoId);
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

	public Usuario registerOrLoginOAuthUser(String email, String name) {
		Usuario user = usuarioRepository.findByEmail(email);
		
		if (user == null) {
			user = new Usuario();
			user.setEmail(email);
			user.setNombre(name);
			usuarioRepository.save(user);
		}
		return user;
	}
	
	public String generateToken(String email) {
		String SECRET_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjMiLCJuYW1lIjoiZWxpZSIsImlhdCI6MTUxNjIzOTAyMn0.jAyqMN8BkaQMlQDpGo6eeI94AmmNR9uSdxgwlqiZLa0";
		byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
		SecretKey secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
		
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
					
	}

}
