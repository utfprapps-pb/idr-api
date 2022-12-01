package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
	//método que fará a busca no banco pelo nome da regiao que foi 
	//passado por parâmetro e retornará uma regiao
	Region findByName(String name);
}
