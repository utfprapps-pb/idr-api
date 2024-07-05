package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.City;

import br.edu.utfpr.ProjetoIDRAPI.specexecutor.CitySpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long>, CitySpecExecutor {
	//método que fará a busca no banco pelo nome da cidade que foi 
	//passado por parâmetro e retornará uma cidade
	City findByName(String name);
}
