package br.edu.utfpr.ProjetoIDRAPI.Test.Service.Impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.CityService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CityServiceImplTest {
	@Autowired
	private CityService service;
	
	@Autowired
	private CityRepository repository;
	
	@Autowired
	private RegionRepository regRepository;
	
	@Test
	public void whenValidFindByName_cityFound() {
		City city = createCity();
		City ct = service.findByName(city.getName());
		
		assertThat(ct.getName()).isEqualTo(city.getName());
	}
	
	@Test
	public void whenValidFindById_cityFound() {
		City city = createCity();
		City ct = service.findOne(city.getId());
		
		assertThat(ct.getId()).isEqualTo(city.getId());
	}
	
	@Test
	public void whenValidFindAll_listCity() {
		Region region2 = new Region(2l, "region2");
		regRepository.save(region2);
		
		City city1 = new City();
		city1.setName("test-city1");
		city1.setCityRegion(region2);
		repository.save(city1);
		
		City city2 = new City();
		city2.setName("test-city2");
		city2.setCityRegion(region2);
		repository.save(city2);
		
		City city3 = new City();
		city3.setName("test-city3");
		city3.setCityRegion(region2);
		repository.save(city3);
		
		List<City> listCities = repository.findAll();
		
		assertThat(listCities).hasSize(4).contains(city1,city2,city3);
	}
	
	private City createCity() {
		Region region = new Region(1l, "region");
		regRepository.save(region);
		
		City city = new City();
		city.setName("test-city");
		city.setCityRegion(region);
		
		repository.save(city);
		return city;
	}
}
