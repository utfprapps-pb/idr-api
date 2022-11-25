package br.edu.utfpr.ProjetoIDRAPI.Test.Service.Impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.RegionService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegionServiceImplTest {
	@Autowired
	private RegionService service;
	
	@Autowired
	private RegionRepository repository;
	
	@Test
	public void whenValidFindByName_regionFound() {
		Region region = createRegion();
		Region reg = service.findByName(region.getName());
		
		assertThat(reg.getName()).isEqualTo(region.getName());
	}
	
	@Test
	public void whenValidFindById_regionFound() {
		Region region = createRegion();
		Region reg = service.findOne(region.getId());
		
		assertThat(reg.getId()).isEqualTo(region.getId());
	}
	
	@Test
	public void whenValidFindAll_listCity() {
		Region region1 = new Region();
		region1.setName("test-region1");
		repository.save(region1);
		Region region2 = new Region();
		region2.setName("test-region2");
		repository.save(region2);
		Region region3 = new Region();
		region3.setName("test-region3");
		repository.save(region3);
		
		List<Region> listRegions = repository.findAll();
		
		assertThat(listRegions).hasSize(3).contains(region1,region2,region3);
	}
	
	private Region createRegion() {
		Region region = new Region();
		region.setName("test-region");
		
		repository.save(region);
		return region;
	}
}
