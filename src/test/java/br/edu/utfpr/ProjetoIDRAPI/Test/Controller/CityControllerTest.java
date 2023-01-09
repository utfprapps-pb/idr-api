package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import br.edu.utfpr.ProjetoIDRAPI.controller.CityController;
import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.CityService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CityController.class)
@AutoConfigureMockMvc
public class CityControllerTest {
	static final String API = "/cities";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CityService service;

	@MockBean
	private CityRepository repository;

	@MockBean
	private RegionRepository regRepository;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	@WithMockUser
	public void whenFindAll_returnSucess() throws Exception {
		regRepository.save(createRegion());
		List<City> records = createList();

		Mockito.when(service.findAll()).thenReturn(records);

		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[2].name", is("City-test-3")));
	}

	@Test
	@WithMockUser
	public void whenFindById_returnCity() throws Exception {
		Mockito.when(service.findOne(createCity().getId())).thenReturn(createCity());

		mvc.perform(MockMvcRequestBuilders.get(API + "/{id}", createCity().getId())).andExpect(status().isOk())
				.andExpect(content().contentType(JSON)).andExpect(jsonPath("$.id", is(4)))
				.andExpect(jsonPath("$.name", is("City-test-4")));
	}

	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(createCity().getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API + "/{id}", createCity().getId())).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser
	public void whenFindByName_returnCity() throws Exception {
		Mockito.when(service.findByName(createCity().getName())).thenReturn(createCity());
		
		mvc.perform(MockMvcRequestBuilders.get(API + "/findName/{name}", createCity().getName()))
		   .andExpect(status().isOk())
		   .andExpect(content().contentType(JSON))
		   .andExpect(jsonPath("$.name", is("City-test-4")));
	}

	@Test
	@WithMockUser
	public void whenFindByNameIsNull_returnBadRequest() throws Exception {
		City city = new City(5l, "", createRegion());

		Mockito.when(service.findByName(city.getName())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API + "/findName", city.getName())).andExpect(status().isBadRequest());
	}

	private List<City> createList() {
		City RECORD_1 = new City(1l, "City-test-1", createRegion());
		City RECORD_2 = new City(2l, "City-test-2", createRegion());
		City RECORD_3 = new City(3l, "City-test-3", createRegion());

		List<City> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);

		return records;
	}

	private City createCity() {
		City city = new City(4l, "City-test-4", createRegion());

		return city;
	}
	
	private Region createRegion() {
		Region region = new Region(1l, "Region");
		
		return region;
	}
}
