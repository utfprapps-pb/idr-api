package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

/*import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import br.edu.utfpr.ProjetoIDRAPI.controller.VegetableDiseaseController;
import br.edu.utfpr.ProjetoIDRAPI.model.Culture;
import br.edu.utfpr.ProjetoIDRAPI.model.Disease;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.model.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetableDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableDiseaseService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = VegetableDiseaseController.class)
@AutoConfigureMockMvc*/
public class VegetableDiseaseControllerTest {
	/*static final String API = "/vegetablediseases";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private VegetableDiseaseRepository repository;
	
	@MockBean
	private VegetableDiseaseService service;
	
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
		List<VegetableDisease> records = createList();
		
		Mockito.when(service.findAll()).thenReturn(records);
		
		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[2].infestationType", is("InfestationType 3")));
	}
	
	@Test
	@WithMockUser
	public void whenFindById_returnVegetable() throws Exception {
		Mockito.when(service.findOne(createVegetable().getId())).thenReturn(createVegetable());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createVegetable().getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.infestationType", is("InfestationType 4")));
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
	public void whenVegetableIsCreated() throws Exception {
		VegetableDisease vegetablePost = createVegetable();
		
		Mockito.when(repository.save(ArgumentMatchers.any(VegetableDisease.class))).thenReturn(vegetablePost);
		
		VegetableDisease vegetableReturn = repository.save(vegetablePost);
		
		assertThat(vegetableReturn.getInfestationType()).isSameAs(vegetablePost.getInfestationType());
		verify(repository).save(vegetablePost);
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldDeleteVegetable() {
		VegetableDisease vegetableDisease = createVegetable();
		
		Mockito.when(repository.findById(vegetableDisease.getId())).thenReturn(Optional.of(vegetableDisease));

		service.delete(vegetableDisease.getId());
		verify(service).delete(vegetableDisease.getId());
	}
	
	@Test
	@WithMockUser
	public void deleteShouldThrowException_whenVegetableDoesntExist() {
		VegetableDisease vegetableDisease = createVegetable();
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		service.delete(vegetableDisease.getId());
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldUpdateVegetable() {
		VegetableDisease vegetableDisease = createVegetable();
		
		Mockito.when(repository.save(ArgumentMatchers.any(VegetableDisease.class))).thenReturn(vegetableDisease);
		
		repository.save(vegetableDisease);
		
		vegetableDisease.setId(4L);
		vegetableDisease.setInfestationType("New Test");
		
		VegetableDisease vegetableUpdate = repository.save(vegetableDisease);
		
		assertThat(vegetableUpdate.getInfestationType()).isEqualTo("New Test");
	}
	
	@Test
	@WithMockUser
	public void updateShouldThrowException_whenVegetableDoesntExist() {
		VegetableDisease vegetableDisease = createVegetable();
		
		VegetableDisease newVegetable = new VegetableDisease();
		newVegetable.setId(0);
		
		vegetableDisease.setInfestationType("New Test");
		
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
	
	public Disease createDisease() {
		Disease disease = new Disease(1l, "Disease 1");
		
		return disease;
	}
	
	public Culture createCulture() {
		Culture culture = new Culture(1l, "Culture 1");
		
		return culture;
	}
	
	private List<VegetableDisease> createList(){
		LocalDate date = LocalDate.parse("2022-06-23");
		
		VegetableDisease vegetable1 = new VegetableDisease();
		vegetable1.setId(1l);
		vegetable1.setInfestationType("InfestationType 1");
		vegetable1.setDate(date);
		vegetable1.setIdProperty(createProperty());
		vegetable1.setIdCulture(createCulture());
		vegetable1.setIdDisease(createDisease());
		
		VegetableDisease vegetable2 = new VegetableDisease();
		vegetable2.setId(2l);
		vegetable2.setInfestationType("InfestationType 2");
		vegetable2.setDate(date);
		vegetable2.setIdProperty(createProperty());
		vegetable2.setIdCulture(createCulture());
		vegetable2.setIdDisease(createDisease());
		
		VegetableDisease vegetable3 = new VegetableDisease();
		vegetable3.setId(3l);
		vegetable3.setInfestationType("InfestationType 3");
		vegetable3.setDate(date);
		vegetable3.setIdProperty(createProperty());
		vegetable3.setIdCulture(createCulture());
		vegetable3.setIdDisease(createDisease());
		
		List<VegetableDisease> records = new ArrayList<>();
		records.add(vegetable1);
		records.add(vegetable2);
		records.add(vegetable3);
		
		return records;
	}
	
	private VegetableDisease createVegetable() {
		LocalDate date = LocalDate.parse("2022-06-23");
		
		VegetableDisease vegetableDisease = new VegetableDisease();
		vegetableDisease.setId(4l);
		vegetableDisease.setInfestationType("InfestationType 4");
		vegetableDisease.setDate(date);
		vegetableDisease.setIdProperty(createProperty());
		vegetableDisease.setIdCulture(createCulture());
		vegetableDisease.setIdDisease(createDisease());
		
		return vegetableDisease;
	}*/
}