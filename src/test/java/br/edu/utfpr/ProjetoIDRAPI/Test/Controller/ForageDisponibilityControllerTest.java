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

import br.edu.utfpr.ProjetoIDRAPI.controller.ForageDisponibilityController;
import br.edu.utfpr.ProjetoIDRAPI.model.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.model.PlagueDisease;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.ForageDisponibilityRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ForageDisponibilityService;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ForageDisponibilityController.class)
@AutoConfigureMockMvc
public class ForageDisponibilityControllerTest {
	static final String API = "/foragesDisponibilities";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ForageDisponibilityRepository repository;
	
	@MockBean
	private ForageDisponibilityService service;
	
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
		List<ForageDisponibility> records = createList();
		
		Mockito.when(service.findAll()).thenReturn(records);
		
		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[2].forage", is("forage-3")));
	}
	
	@Test
	@WithMockUser
	public void whenFindById_returnForageDisponibility() throws Exception {
		Mockito.when(service.findOne(createForageDisponibility().getId())).thenReturn(createForageDisponibility());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createForageDisponibility().getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.forage", is("forage-4")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(createForageDisponibility().getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createForageDisponibility().getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser
	public void whenForageDisponibilityIsCreated() throws Exception {
		ForageDisponibility forageDisponibilityPost = createForageDisponibility();
		
		Mockito.when(repository.save(ArgumentMatchers.any(ForageDisponibility.class))).thenReturn(forageDisponibilityPost);
		
		ForageDisponibility forageDisponibilityReturn = repository.save(forageDisponibilityPost);
		
		assertThat(forageDisponibilityReturn.getForage()).isSameAs(forageDisponibilityPost.getForage());
		verify(repository).save(forageDisponibilityPost);
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldDeleteForageDisponibility() {
		ForageDisponibility forageDisponibility = createForageDisponibility();
		
		Mockito.when(repository.findById(forageDisponibility.getId())).thenReturn(Optional.of(forageDisponibility));

		service.delete(forageDisponibility.getId());
		verify(service).delete(forageDisponibility.getId());
	}
	
	@Test
	@WithMockUser
	public void deleteShouldThrowException_whenForageDisponibilityDoesntExist() {
		ForageDisponibility forageDisponibility = createForageDisponibility();
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		service.delete(forageDisponibility.getId());
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldUpdateForageDisponibility() {
		ForageDisponibility forageDisponibility = createForageDisponibility();
		
		Mockito.when(repository.save(ArgumentMatchers.any(ForageDisponibility.class))).thenReturn(forageDisponibility);
		
		repository.save(forageDisponibility);
		
		forageDisponibility.setId(4L);
		forageDisponibility.setForage("New Test forage");
		
		ForageDisponibility forageDisponibilityUpdate = repository.save(forageDisponibility);
		
		assertThat(forageDisponibilityUpdate.getForage()).isEqualTo("New Test forage");
	}
	
	@Test
	@WithMockUser
	public void updateShouldThrowException_whenForageDisponibilityDoesntExist() {
		ForageDisponibility forageDisponibility = createForageDisponibility();
		
		ForageDisponibility newForageDisponibility = new ForageDisponibility();
		newForageDisponibility.setId(0);
		
		forageDisponibility.setForage("New Test forage");
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		repository.save(newForageDisponibility);
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
	
	private List<ForageDisponibility> createList(){
		ForageDisponibility RECORD_1 = new ForageDisponibility();
		RECORD_1.setId(1l);
		RECORD_1.setForage("forage-1");
		RECORD_1.setProperty(createProperty());
		
		ForageDisponibility RECORD_2 = new ForageDisponibility();
		RECORD_2.setId(2l);
		RECORD_2.setForage("forage-2");
		RECORD_2.setProperty(createProperty());
		
		ForageDisponibility RECORD_3 = new ForageDisponibility();
		RECORD_3.setId(3l);
		RECORD_3.setForage("forage-3");
		RECORD_3.setProperty(createProperty());
		
		List<ForageDisponibility> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);
		
		return records;
	}
	
	private ForageDisponibility createForageDisponibility() {
		ForageDisponibility forageDisponibility = new ForageDisponibility();
		forageDisponibility.setId(4l);
		forageDisponibility.setForage("forage-4");
		forageDisponibility.setProperty(createProperty());
		
		return forageDisponibility;
	}
}