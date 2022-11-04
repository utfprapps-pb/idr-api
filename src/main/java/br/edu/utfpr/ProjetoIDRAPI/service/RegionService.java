package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;

public interface RegionService extends CrudService<Region, Long>{
	Region findByName(String name);
}
