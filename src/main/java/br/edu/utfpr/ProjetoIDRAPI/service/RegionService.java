package br.edu.utfpr.ProjetoIDRAPI.service;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;

public interface RegionService {
	Region findOneById(Long id);
	Region findOneByNome(String name);
	List<Region> findAll();
}
