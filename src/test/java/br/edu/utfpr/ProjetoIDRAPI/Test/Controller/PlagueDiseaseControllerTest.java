package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import br.edu.utfpr.ProjetoIDRAPI.controller.PlagueDiseaseController;
import br.edu.utfpr.ProjetoIDRAPI.model.PlagueDisease;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;
import br.edu.utfpr.ProjetoIDRAPI.repository.PlagueDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PlagueDiseaseService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = PlagueDiseaseController.class)
@AutoConfigureMockMvc
public class PlagueDiseaseControllerTest {
	static final String API = "/plaguesDiseases";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PlagueDiseaseRepository repository;
	
	@MockBean
	private PlagueDiseaseService service;
	
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
		List<PlagueDisease> records = createList();
		
		Mockito.when(service.findAll()).thenReturn(records);
		
		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[2].identification", is("ident-3")));
	}
	
	@Test
	@WithMockUser
	public void whenFindById_returnPlagueDisease() throws Exception {
		Mockito.when(service.findOne(createPlagueDisease().getId())).thenReturn(createPlagueDisease());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createPlagueDisease().getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.identification", is("ident-4")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(createPlagueDisease().getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createPlagueDisease().getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser
	public void whenPlagueDiseaseIsCreated() throws Exception {
		PlagueDisease plagueDiseasePost = createPlagueDisease();
		
		Mockito.when(repository.save(ArgumentMatchers.any(PlagueDisease.class))).thenReturn(plagueDiseasePost);
		
		PlagueDisease plagueDiseaseReturn = repository.save(plagueDiseasePost);
		
		assertThat(plagueDiseaseReturn.getIdentification()).isSameAs(plagueDiseasePost.getIdentification());
		verify(repository).save(plagueDiseasePost);
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldDeletePlagueDisease() {
		PlagueDisease plagueDisease = createPlagueDisease();
		
		Mockito.when(repository.findById(plagueDisease.getId())).thenReturn(Optional.of(plagueDisease));

		service.delete(plagueDisease.getId());
		verify(service).delete(plagueDisease.getId());
	}
	
	@Test
	@WithMockUser
	public void deleteShouldThrowException_whenPlagueDiseaseDoesntExist() {
		PlagueDisease plagueDisease = createPlagueDisease();
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		service.delete(plagueDisease.getId());
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldUpdatePlagueDisease() {
		PlagueDisease plagueDisease = createPlagueDisease();
		
		repository.save(plagueDisease);
		
		plagueDisease.setId(4L);
		plagueDisease.setIdentification("New Test Ident");
		
		repository.save(plagueDisease);
		
		assertThat(plagueDisease.getIdentification()).isEqualTo("New Test Ident");
	}
	
	@Test
	@WithMockUser
	public void updateShouldThrowException_whenPlagueDiseaseDoesntExist() {
		PlagueDisease plagueDisease = createPlagueDisease();
		
		PlagueDisease newplagueDisease = new PlagueDisease();
		newplagueDisease.setId(null);
		
		plagueDisease.setIdentification("New Test Ident");
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		repository.save(newplagueDisease);
	}
	
	public Vegetable createVegetable() {
		User user = new User(1l, "User-test-1", "1717", null, null, null, null, "21221", null, "12222", null, null);
		Property property = new Property(1l,user,null,null,null,null,null,true);
		Vegetable vegetable = new Vegetable(1l,property,"test-4");
		
		return vegetable;
	}
	
	public List<PlagueDisease> createList(){
		PlagueDisease RECORD_1 = new PlagueDisease(1l,createVegetable(),null,"ident-1",null);
		PlagueDisease RECORD_2 = new PlagueDisease(2l,createVegetable(),null,"ident-2",null);
		PlagueDisease RECORD_3 = new PlagueDisease(3l,createVegetable(),null,"ident-3",null);
		
		List<PlagueDisease> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);
		
		return records;
	}
	
	public PlagueDisease createPlagueDisease() {
		PlagueDisease plagueDisease = new PlagueDisease(4l,createVegetable(),null,"ident-4",null);
		
		return plagueDisease;
	}
}
