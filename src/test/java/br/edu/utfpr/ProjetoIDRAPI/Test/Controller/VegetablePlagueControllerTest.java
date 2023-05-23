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

import br.edu.utfpr.ProjetoIDRAPI.controller.VegetablePlagueController;
import br.edu.utfpr.ProjetoIDRAPI.model.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.model.Culture;
import br.edu.utfpr.ProjetoIDRAPI.model.Plague;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetablePlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetablePlagueService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = VegetablePlagueController.class)
@AutoConfigureMockMvc*/
public class VegetablePlagueControllerTest {
	/*static final String API = "/vegetableplagues";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private VegetablePlagueRepository repository;
	
	@MockBean
	private VegetablePlagueService service;
	
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
		List<VegetablePlague> records = createList();
		
		Mockito.when(service.findAll()).thenReturn(records);
		
		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[2].infestationType", is("InfestationType 3")));
	}
	
	@Test
	@WithMockUser
	public void whenFindById_returnPlague() throws Exception {
		Mockito.when(service.findOne(createVegetablePlague().getId())).thenReturn(createVegetablePlague());
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createVegetablePlague().getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(4)))
        .andExpect(jsonPath("$.infestationType", is("InfestationType 4")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(createVegetablePlague().getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", createVegetablePlague().getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser
	public void whenPlagueIsCreated() throws Exception {
		VegetablePlague plaguePost = createVegetablePlague();
		
		Mockito.when(repository.save(ArgumentMatchers.any(VegetablePlague.class))).thenReturn(plaguePost);
		
		VegetablePlague plagueDiseaseReturn = repository.save(plaguePost);
		
		assertThat(plagueDiseaseReturn.getInfestationType()).isSameAs(plaguePost.getInfestationType());
		verify(repository).save(plaguePost);
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldDeletePlague() {
		VegetablePlague vegetablePlague = createVegetablePlague();
		
		Mockito.when(repository.findById(vegetablePlague.getId())).thenReturn(Optional.of(vegetablePlague));

		service.delete(vegetablePlague.getId());
		verify(service).delete(vegetablePlague.getId());
	}
	
	@Test
	@WithMockUser
	public void deleteShouldThrowException_whenPlagueDoesntExist() {
		VegetablePlague vegetablePlague = createVegetablePlague();
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		service.delete(vegetablePlague.getId());
	}
	
	@Test
	@WithMockUser
	public void whenGivenId_shouldUpdatePlague() {
		VegetablePlague vegetablePlague = createVegetablePlague();
		
		Mockito.when(repository.save(ArgumentMatchers.any(VegetablePlague.class))).thenReturn(vegetablePlague);
		
		repository.save(vegetablePlague);
		
		vegetablePlague.setId(4L);
		vegetablePlague.setInfestationType("New Test Inf");
		
		VegetablePlague plagueUpdate = repository.save(vegetablePlague);
		
		assertThat(plagueUpdate.getInfestationType()).isEqualTo("New Test Inf");
	}
	
	@Test
	@WithMockUser
	public void updateShouldThrowException_whenPlagueDoesntExist() {
		VegetablePlague vegetablePlague = createVegetablePlague();
		
		VegetablePlague newplague = new VegetablePlague();
		newplague.setId(0);
		
		vegetablePlague.setInfestationType("New Test Inf");
		
		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		repository.save(newplague);
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
	
	public Culture createCulture() {
		Culture culture = new Culture(1l, "Culture 1");
		
		return culture;
	}
	
	public Plague createPlague() {
		Plague plague = new Plague(1l, "Plague 1");
		
		return plague;
	}
	
	private VegetablePlague createVegetablePlague() {
		LocalDate date = LocalDate.parse("2022-06-23");
		
		VegetablePlague vegetablePlague = new VegetablePlague();
		vegetablePlague.setId(4l);
		vegetablePlague.setInfestationType("InfestationType 4");
		vegetablePlague.setDate(date);
		vegetablePlague.setIdProperty(createProperty());
		vegetablePlague.setIdCulture(createCulture());
		vegetablePlague.setIdPlague(createPlague());
		
		return vegetablePlague;
	}
	
	private List<VegetablePlague> createList(){
		LocalDate date = LocalDate.parse("2022-06-23");
		
		VegetablePlague vegetablePlague1 = new VegetablePlague();
		vegetablePlague1.setId(1l);
		vegetablePlague1.setInfestationType("InfestationType 1");
		vegetablePlague1.setDate(date);
		vegetablePlague1.setIdProperty(createProperty());
		vegetablePlague1.setIdCulture(createCulture());
		vegetablePlague1.setIdPlague(createPlague());
		
		VegetablePlague vegetablePlague2 = new VegetablePlague();
		vegetablePlague2.setId(2l);
		vegetablePlague2.setInfestationType("InfestationType 2");
		vegetablePlague2.setDate(date);
		vegetablePlague2.setIdProperty(createProperty());
		vegetablePlague2.setIdCulture(createCulture());
		vegetablePlague2.setIdPlague(createPlague());
		
		VegetablePlague vegetablePlague3 = new VegetablePlague();
		vegetablePlague3.setId(3l);
		vegetablePlague3.setInfestationType("InfestationType 3");
		vegetablePlague3.setDate(date);
		vegetablePlague3.setIdProperty(createProperty());
		vegetablePlague3.setIdCulture(createCulture());
		vegetablePlague3.setIdPlague(createPlague());
		
		List<VegetablePlague> records = new ArrayList<>();
		records.add(vegetablePlague1);
		records.add(vegetablePlague2);
		records.add(vegetablePlague3);
		
		return records;
	}*/
}