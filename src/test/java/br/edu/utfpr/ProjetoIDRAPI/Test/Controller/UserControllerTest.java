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

		repository.save(user);

		user.setId(4L);
		user.setUsername("New Test Name");

		repository.save(user);

		assertThat(user.getUsername()).isEqualTo("New Test Name");
	}

	@Test
	@WithMockUser
	public void updateShouldThrowException_whenUserDoesntExist() {
		User user = createUser();

		User newUser = new User();
		newUser.setId(null);
		user.setUsername("New Test Name");

		Mockito.when(service.findOne(anyLong())).thenReturn(null);
		repository.save(newUser);
	}
	
	public List<User> createList(){
		User RECORD_1 = new User(1l, "User-test-1", "1616", null, null, null, null, "21221", null, "12222", null, null);
		User RECORD_2 = new User(2l, "User-test-2", "1313", null, null, null, null, "13131", null, "16161", null, null);
		User RECORD_3 = new User(3l, "User-test-3", "9444", null, null, null, null, "11551", null, "13113", null, null);
		
		List<User> records = new ArrayList<>();
		records.add(RECORD_1);
		records.add(RECORD_2);
		records.add(RECORD_3);
		
		return records;
	}
	
	public User createUser(){
		User user = new User(4l, "User-test-4", "1717", null, null, null, null, "21221", null, "12222", null, null);
		
		return user;
	}
}
