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

import br.edu.utfpr.ProjetoIDRAPI.controller.VegetableController;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetableRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = VegetableController.class)
@AutoConfigureMockMvc
public class VegetableControllerTest {
	static final String API = "/vegetables";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private VegetableRepository repository;
	
	@MockBean
	private VegetableService service;
	
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
		List<Vegetable> records = createList();
		
		Mockito.when(service.findAll()).thenReturn(records);
		
		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[2].name", is("test-3")));
	}
	
	@Test
	@WithMockUser
	public void whenFindById_returnVegetable() throws Exception {
		Mockito.when(service.findOne(createVegetable().getId())).thenReturn(createVegetable());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createVegetable().getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.name", is("test-4")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(createVegetable().getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createVegetable().getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser
	public void whenFindByName_returnVegetable() throws Exception {
		Mockito.when(service.findByName(createVegetable().getName())).thenReturn(createVegetable());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/findName/{name}", createVegetable().getName()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.name", is("test-4")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByNameIsNull_returnBadRequest() throws Exception {
		Vegetable vegetable = new Vegetable(5l,createProperty(),"");
		
		Mockito.when(service.findByName(vegetable.getName())).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/findName/{name}", vegetable.getName()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void whenVegetableIsCreated() throws Exception {
		Vegetable vegetablePost = createVegetable();
		
		Mockito.when(repository.save(ArgumentMatchers.any(Vegetable.class))).thenReturn(vegetablePost);
		
		Vegetable vegetableReturn = repository.save(vegetablePost);
		
		assertThat(vegetableReturn.getName()).isSameAs(vegetablePost.getName());
		verify(repository).save(vegetablePost);
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldDeleteVegetable() {
		Vegetable vegetable = createVegetable();
		
		Mockito.when(repository.findById(vegetable.getId())).thenReturn(Optional.of(vegetable));

		service.delete(vegetable.getId());
		verify(service).delete(vegetable.getId());
	}
	
	@Test
	@WithMockUser
	public void deleteShouldThrowException_whenVegetableDoesntExist() {
		Vegetable vegetable = createVegetable();
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		service.delete(vegetable.getId());
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldUpdateVegetable() {
		Vegetable vegetable = createVegetable();
		
		Mockito.when(repository.save(ArgumentMatchers.any(Vegetable.class))).thenReturn(vegetable);
		
		repository.save(vegetable);
		
		vegetable.setId(4L);
		vegetable.setName("New Test Name");
		
		Vegetable vegetableUpdate = repository.save(vegetable);
		
		assertThat(vegetableUpdate.getName()).isEqualTo("New Test Name");
	}
	
	@Test
	@WithMockUser
	public void updateShouldThrowException_whenVegetableDoesntExist() {
		Vegetable vegetable = createVegetable();
		
		Vegetable newVegetable = new Vegetable();
		newVegetable.setId(0);
		
		vegetable.setName("New Test Name");
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		repository.save(newVegetable);
	}
	
	private User createUser() {
		User user = new User();
		user.setId(1l);
		user.setUsername("User-test-1");
		user.setPassword("1717");
		user.setPhone("21221");
		user.setProfessionalRegister("12222");
		
		return user;
	}
	
	private Property createProperty() {
		Property property = new Property();
		property.setId(1l);
		property.setUser(createUser());
		property.setLeased(true);
		
		return property;
	}
	
	private List<Vegetable> createList(){
		Vegetable RECORD_1 = new Vegetable(1l,createProperty(),"test-1");
		Vegetable RECORD_2 = new Vegetable(2l,createProperty(),"test-2");
		Vegetable RECORD_3 = new Vegetable(3l,createProperty(),"test-3");
		
		List<Vegetable> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);
		
		return records;
	}
	
	private Vegetable createVegetable() {
		Vegetable vegetable = new Vegetable(4l,createProperty(),"test-4");
		
		return vegetable;
	}
}