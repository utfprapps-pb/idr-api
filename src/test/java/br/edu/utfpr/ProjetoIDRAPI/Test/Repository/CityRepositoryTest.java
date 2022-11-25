package br.edu.utfpr.ProjetoIDRAPI.Test.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CityRepositoryTest {
	@Autowired
	private CityRepository repository;
	
	private City city;
	
	@Autowired
	private RegionRepository regRepository;
	private Region region;
	
	@BeforeEach
	public void setup() {
		region = Region.builder().name("region").build();
		regRepository.save(region);
		city = City.builder().id(1l).name("test-repository-City").cityRegion(region).build();
		repository.save(city);
	}
	
	@Test
	public void whenFindAll_thenReturnsList() {
		City ct = city.builder().name("test-List-City").cityRegion(region).build();
		
		repository.save(ct);
		
		List<City> cityList = repository.findAll();
		
		assertThat(cityList).isNotNull();
		assertThat(cityList.size()).isEqualTo(2);
	}
	
	@Test
	public void whenFindById_returnRegionObject() {
		City cityFind = repository.findById(city.getId()).get();
		System.out.println(cityFind);
		
		assertThat(cityFind).isNotNull();
	}
}
