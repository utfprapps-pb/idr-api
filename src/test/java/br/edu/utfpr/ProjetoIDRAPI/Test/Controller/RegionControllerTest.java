package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import br.edu.utfpr.ProjetoIDRAPI.controller.RegionController;
import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.RegionService;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = RegionController.class)
@AutoConfigureMockMvc
public class RegionControllerTest {
	static final String API = "/Regions";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@MockBean
	private RegionRepository repository;
	
	@MockBean
	private RegionService service;
	
	Region RECORD_1 = new Region(1l, "Region-test-1");
	Region RECORD_2 = new Region(2l, "Region-test-2");
	Region RECORD_3 = new Region(3l, "Region-test-3");
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
	}

	@Test
	@WithMockUser
	public void whenFingAll_returnSucess() throws Exception {
		List<Region> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);
		
		Mockito.when(service.findAll()).thenReturn(records);
		
		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[2].name", is("Region-test-3")));
	}
	
	@Test
	@WithMockUser
	public void whenFindById_returnRegion() throws Exception {
		Mockito.when(service.findOne(RECORD_1.getId())).thenReturn(RECORD_1);
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", RECORD_1.getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Region-test-1")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(RECORD_1.getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API+"/{id}", RECORD_1.getId()))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser
	public void whenFindByName_returnRegion() throws Exception {
		Mockito.when(service.findByName(RECORD_1.getName())).thenReturn(RECORD_1);
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/findName/{name}", RECORD_1.getName()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(JSON))
		.andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Region-test-1")));
	}
	
	@Test
	@WithMockUser
	public void whenFindByNameIsNull_returnBadRequest() throws Exception {
		Region region = new Region(4l, "");
		
		Mockito.when(service.findByName(region.getName())).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(API+"/findName/{name}", region.getName()))
		.andExpect(status().isBadRequest());
	}
}
