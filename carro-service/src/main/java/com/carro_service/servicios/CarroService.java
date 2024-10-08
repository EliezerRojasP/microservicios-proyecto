package com.carro_service.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carro_service.entidades.Carro;
import com.carro_service.repositorio.CarroRepository;

@Service
public class CarroService {
		
	@Autowired
	private CarroRepository carroRepository;
	
	public List<Carro> getAll(){
		return carroRepository.findAll();
	}
	
	public Carro getCarroById(int id){
		return carroRepository.findById(id).orElse(null);
	}
	
	public Carro save(Carro carro){
		Carro nuevoCarro = carroRepository.save(carro);
		return nuevoCarro;
	}
	
	public List<Carro> findByUsuarioId(int usuarioId){
		return carroRepository.findByUsuarioId(usuarioId);
	}
	
	public Carro updateCarro(int id, Carro updatedCarro) {
		return carroRepository.findById(id).map(carro -> {
			
			carro.setMarca(updatedCarro.getMarca());
			carro.setModelo(updatedCarro.getModelo());

			return carroRepository.save(carro);
		}).orElse(null);
	}

	public boolean deleteCarro(int id) {
		if (carroRepository.existsById(id)) {
			carroRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
