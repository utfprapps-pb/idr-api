package br.edu.utfpr.ProjetoIDRAPI.entity.region;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;

public interface RegionService extends CrudService<Region, Long> {

	Region findByName(String name);

}
