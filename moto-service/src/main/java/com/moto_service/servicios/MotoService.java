package com.moto_service.servicios;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moto_service.entidades.Moto;
import com.moto_service.repositorio.MotoRepository;

@Service
public class MotoService {
	
	@Autowired
	private MotoRepository motoRepository;
	
	public List<Moto> getAll(){
		return motoRepository.findAll();
	}
	
	public Moto getMotoById(int id) {
		return motoRepository.findById(id).orElse(null);
	}
	
	public Moto save(Moto moto) {
		Moto nuevaMoto = motoRepository.save(moto);
		return nuevaMoto;
	}
	
	public List<Moto> findByUsuarioId(int usuarioId){
		return motoRepository.findByUsuarioId(usuarioId);
	}
	
	public Moto updateMoto(int id, Moto updatedMoto) {
		return motoRepository.findById(id).map(moto -> {
			
			moto.setMarca(updatedMoto.getMarca());
			moto.setModelo(updatedMoto.getModelo());
			
			return motoRepository.save(moto); 
		}).orElse(null); 
	}
	
	public void deleteMoto(int id) {
		if (motoRepository.existsById(id)) {
			motoRepository.deleteById(id);
		} else {
			throw new NoSuchElementException("La moto con el ID " + id + " no existe.");
		}
	}
}
