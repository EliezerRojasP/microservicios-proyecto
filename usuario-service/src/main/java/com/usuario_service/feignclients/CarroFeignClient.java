package com.usuario_service.feignclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.carro_service.entidades.Carro;

@FeignClient(name = "carro-service", url = "http://localhost:8002")
public interface CarroFeignClient {
	
	@PostMapping("/carro")
	public Carro save(@RequestBody Carro carro);
	
	@GetMapping("/carro/usuario/{usuarioId}")
	public List<Carro> getCarros(@PathVariable("usuarioId") int usuarioId);
}
