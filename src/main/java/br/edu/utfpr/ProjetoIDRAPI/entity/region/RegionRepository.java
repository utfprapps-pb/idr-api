package br.edu.utfpr.ProjetoIDRAPI.entity.region;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long>, RegionSpecExecutor {
	//método que fará a busca no banco pelo nome da regiao que foi 
	//passado por parâmetro e retornará uma regiao
	Region findByname(String name);
}
