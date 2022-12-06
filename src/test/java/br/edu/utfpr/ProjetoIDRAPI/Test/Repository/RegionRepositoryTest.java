package br.edu.utfpr.ProjetoIDRAPI.Test.Repository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegionRepositoryTest {
	@Autowired
	private RegionRepository repository;
	
	private Region region;
	
	@BeforeEach
	public void setup() {
		region = Region.builder().id(1l).name("test-repository-region").build();
		
	}
	
	@Test
	public void whenFindAll_thenReturnsList() {
		Region reg = Region.builder().name("test-List-Region").build();
		
		repository.save(region);
		repository.save(reg);
		
		List<Region> regionList = repository.findAll();
		
		assertThat(regionList).isNotNull();
		assertThat(regionList.size()).isEqualTo(2);
	}
	
	@Test
	public void whenFindById_returnRegionObject() {
		repository.save(region);
		
		Region regFind = repository.findById(region.getId()).get();
		
		assertThat(regFind).isNotNull();
	}
	
	@Test
	public void whenFindByName_returnRegionObject() {
		Region regFind = repository.findByName(region.getName());
		System.out.println(regFind);
		
		assertThat(regFind).isNotNull();
	}
}
