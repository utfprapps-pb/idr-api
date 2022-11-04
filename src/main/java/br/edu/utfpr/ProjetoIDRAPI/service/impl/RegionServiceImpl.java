package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.RegionService;

@Service
public class RegionServiceImpl implements RegionService{
	private final RegionRepository regionRepository;
	
	public RegionServiceImpl(RegionRepository regionRepository) {
		this.regionRepository = regionRepository;
	}

	@Override
	public Region findOne(Long id) {
		return regionRepository.findById(id).orElse(null);
	}

	@Override
	public Region findByName(String name) {
		return regionRepository.findByName(name);
	}

	@Override
	public List<Region> findAll() {
		return regionRepository.findAll();
	}

	@Override
	public Region save(Region entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
	}
}
