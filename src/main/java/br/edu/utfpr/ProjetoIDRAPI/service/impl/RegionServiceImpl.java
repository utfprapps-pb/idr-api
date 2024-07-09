package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.RegionService;

@Service
public class RegionServiceImpl extends CrudServiceImpl<Region, Long> implements RegionService {

	private final RegionRepository regionRepository;
	
	public RegionServiceImpl(RegionRepository regionRepository) {
		this.regionRepository = regionRepository;
	}

	@Override
	protected JpaRepository<Region, Long> getRepository() {
		return this.regionRepository;
	}

	@Override
	public Region findByName(String name) {
		return regionRepository.findByname(name);
	}

	@Override
	public JpaSpecificationExecutor<Region> getSpecExecutor() {
		return this.regionRepository;
	}
}
