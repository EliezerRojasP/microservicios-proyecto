package com.usuario_service.feignclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.moto_service.entidades.Moto;

@FeignClient(name = "moto-service")
public interface MotoFeignClient {
	
	@PostMapping("/moto")
	public Moto save(@RequestBody Moto moto);
	
	@GetMapping("/moto/usuario/{usuarioId}")
	public List<Moto> getMotos(@PathVariable("usuarioId") int usuarioId);
	
	@PutMapping("/moto/{id}")
	public Moto updateMoto(@PathVariable("id") int id, @RequestBody Moto moto);
	
	@GetMapping("/moto/{id}")
	public Moto getMotoById(@PathVariable("id") int id);
	
	@DeleteMapping("/moto/{id}")
	void deleteMoto(@PathVariable("id") int id);
}
