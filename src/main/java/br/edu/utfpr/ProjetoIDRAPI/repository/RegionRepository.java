package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
	Region findByName(String name);
}
