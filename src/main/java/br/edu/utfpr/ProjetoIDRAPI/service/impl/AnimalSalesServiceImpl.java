package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalSales;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalSalesRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalSalesService;

@Service
public class AnimalSalesServiceImpl extends CrudServiceImpl<AnimalSales, Long> implements AnimalSalesService {
	private final AnimalSalesRepository animalSalesRepository;
	
	public AnimalSalesServiceImpl(AnimalSalesRepository animalSalesRepository) {
		this.animalSalesRepository = animalSalesRepository;
	}

	@Override
	protected JpaRepository<AnimalSales, Long> getRepository() {
		return this.animalSalesRepository;
	}
}
