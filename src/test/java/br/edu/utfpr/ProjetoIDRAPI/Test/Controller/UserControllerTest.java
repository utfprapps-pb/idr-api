package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

import br.edu.utfpr.ProjetoIDRAPI.controller.UserController;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.UserService;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
	static final String API = "/users";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService service;

	@MockBean
	private UserRepository repository;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	@Test
	@WithMockUser
	public void whenFindAll_returnSucess() throws Exception {
		List<User> records = createList();

		Mockito.when(service.findAll()).thenReturn(records);

		mvc.perform(MockMvcRequestBuilders.get(API).contentType(JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[2].username", is("User-test-3")));
	}

	@Test
	@WithMockUser
	public void whenFindById_returnUser() throws Exception {
		Mockito.when(service.findOne(createUser().getId())).thenReturn(createUser());

		mvc.perform(MockMvcRequestBuilders.get(API + "/{id}", createUser().getId())).andExpect(status().isOk())
				.andExpect(content().contentType(JSON)).andExpect(jsonPath("$.id", is(4)))
				.andExpect(jsonPath("$.username", is("User-test-4")));
	}

	@Test
	@WithMockUser
	public void whenFindByIdIsNull_returnNoContentStatus() throws Exception {
		Mockito.when(service.findOne(createUser().getId())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API + "/{id}", createUser().getId())).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser
	public void whenFindByName_returnUser() throws Exception {
		Mockito.when(service.findByName(createUser().getUsername())).thenReturn(createUser());

		mvc.perform(MockMvcRequestBuilders.get(API + "/findName/{username}", createUser().getUsername()))
				.andExpect(status().isOk()).andExpect(content().contentType(JSON)).andExpect(jsonPath("$.id", is(4)))
				.andExpect(jsonPath("$.username", is("User-test-4")));
	}

	@Test
	@WithMockUser
	public void whenFindByNameIsNull_returnBadRequest() throws Exception {
		User user = new User(5l, "", "9444", null, null, null, null, "11551", null, "13113", null, null);

		Mockito.when(service.findByName(user.getUsername())).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get(API + "/findName/{username}", user.getUsername()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	public void whenUserIsCreated() throws Exception {
		User userToPost = createUser();

		Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(userToPost);

		User userToReturn = repository.save(userToPost);

		assertThat(userToReturn.getUsername()).isSameAs(userToPost.getUsername());
		verify(repository).save(userToPost);
	}

	@Test
	@WithMockUser
	public void whenGivenId_shouldDeleteUser() {
		User user = createUser();

		Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

		service.delete(user.getId());
		verify(service).delete(user.getId());
	}

	@Test
	@WithMockUser
	public void deleteShouldThrowException_whenUserDoesntExist() {
		User user = createUser();

		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		service.delete(user.getId());
	}

	@Test
	@WithMockUser
	public void whenGivenId_shouldUpdateUser() {
		User user = createUser();
		
		Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
		
		repository.save(user);

		user.setId(4L);
		user.setUsername("New Test Name");

		User userUpdate = repository.save(user);

		assertThat(userUpdate.getUsername()).isEqualTo("New Test Name");
	}

	@Test
	@WithMockUser
	public void updateShouldThrowException_whenUserDoesntExist() {
		User user = createUser();

		User newUser = new User();
		newUser.setId(0);
		user.setUsername("New Test Name");

		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		repository.save(newUser);
	}
	
	private List<User> createList(){
		User RECORD_1 = new User();
		RECORD_1.setId(1l);  
		RECORD_1.setUsername("User-test-1");  
		RECORD_1.setCpf("1616");
		RECORD_1.setPhone("21221");
		RECORD_1.setProfessionalRegister("12222");
		
		User RECORD_2 = new User();
		RECORD_2.setId(2l);  
		RECORD_2.setUsername("User-test-2");
		RECORD_2.setCpf("1313");
		RECORD_2.setPhone("13131");
		RECORD_2.setProfessionalRegister("16161");
		
		User RECORD_3 = new User();
		RECORD_3.setId(3l);  
		RECORD_3.setUsername("User-test-3");
		RECORD_3.setCpf("9444");
		RECORD_3.setPhone("11551");
		RECORD_3.setProfessionalRegister("13113");
		
		List<User> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);
		
		return records;
	}
	
	private User createUser(){
		User user = new User();
		user.setId(4l);
		user.setUsername("User-test-4");
		user.setCpf("1717");
		user.setPhone("21221");
		user.setProfessionalRegister("12222");
		
		return user;
	}
}
