package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

/*import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import br.edu.utfpr.ProjetoIDRAPI.controller.RegionController;
import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.RegionService;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = RegionController.class)
@AutoConfigureMockMvc*/
public class RegionControllerTest {
	/*static final String API = "/regions";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RegionRepository repository;
	
	@MockBean
	private RegionService service;
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
	}

	@Test
	@WithMockUser
	public void whenFindAll_returnSucess() throws Exception {
		List<Region> records = createList();
		
		Mockito.when(service.findAll()).thenReturn(records);
		
		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[2].name", is("Region-test-3")));
	}
	
	@Test
	@WithMockUser
	public void whenFindById_returnRegion() throws Exception {
		Mockito.when(service.findOne(createRegion().getId())).thenReturn(createRegion());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createRegion().getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.name", is("Region-test-4")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(createRegion().getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createRegion().getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser
	public void whenFindByName_returnRegion() throws Exception {
		Mockito.when(service.findByName(createRegion().getName())).thenReturn(createRegion());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/findName/{name}", createRegion().getName()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.name", is("Region-test-4")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByNameIsNull_returnBadRequest() throws Exception {
		Region region = new Region(5l, "");
		
		Mockito.when(service.findByName(region.getName())).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/findName/{name}", region.getName()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void whenRegionIsCreated() throws Exception {
		Region regionPost = createRegion();
		
		Mockito.when(repository.save(ArgumentMatchers.any(Region.class))).thenReturn(regionPost);
		
		Region regionReturn = repository.save(regionPost);
		
		assertThat(regionReturn.getName()).isSameAs(regionPost.getName());
		verify(repository).save(regionPost);
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldDeleteRegion() {
		Region region = createRegion();
		
		Mockito.when(repository.findById(region.getId())).thenReturn(Optional.of(region));

		service.delete(region.getId());
		verify(service).delete(region.getId());
	}
	
	@Test
	@WithMockUser
	public void deleteShouldThrowException_whenRegionDoesntExist() {
		Region region = createRegion();
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		service.delete(region.getId());
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldUpdateRegion() {
		Region region = createRegion();
		
		Mockito.when(repository.save(ArgumentMatchers.any(Region.class))).thenReturn(region);
		
		repository.save(region);
		
		region.setId(4L);
		region.setName("New Test Name");
		
		Region regionUpdate = repository.save(region);
		
		assertThat(regionUpdate.getName()).isEqualTo("New Test Name");
	}
	
	@Test
	@WithMockUser
	public void updateShouldThrowException_whenRegionDoesntExist() {
		Region region = createRegion();
		
		Region newRegion = new Region();
		newRegion.setId(0);
		
		region.setName("New Test Name");
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		repository.save(newRegion);
	}
	
	private List<Region> createList(){
		Region RECORD_1 = new Region(1l, "Region-test-1");
		Region RECORD_2 = new Region(2l, "Region-test-2");
		Region RECORD_3 = new Region(3l, "Region-test-3");
		
		List<Region> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);
		
		return records;
	}
	
	private Region createRegion() {
		Region region = new Region(4l, "Region-test-4");
		
		return region;
	}*/
}
