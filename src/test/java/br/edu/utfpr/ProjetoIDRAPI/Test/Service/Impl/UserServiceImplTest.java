package br.edu.utfpr.ProjetoIDRAPI.Test.Service.Impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserServiceImplTest {
	@Autowired
	private UserService service;
	
	@Autowired
	private UserRepository repository;
	
	@Test
	public void whenValidFindByName_userFound() {
		User user = createUser();
		User us = service.findByName(user.getUsername());
		
		assertThat(us.getUsername()).isEqualTo(user.getUsername());
	}
	
	@Test
	public void whenValidFindById_userFound() {
		User user = createUser();
		User us = service.findOne(user.getId());
		
		assertThat(us.getId()).isEqualTo(user.getId());
	}
	
	@Test
	public void whenValidFindAll_listCity() {
		User user1 = new User();
		user1.setUsername("test-user1");
		user1.setCpf("511515155115");
		user1.setPhone("5115151511");
		user1.setProfessionalRegister("5151511511");
		user1.setUserPermissions(null);
		repository.save(user1);
		
		User user2 = new User();
		user2.setUsername("test-user2");
		user2.setCpf("511515155115");
		user2.setPhone("5115151511");
		user2.setProfessionalRegister("5151511511");
		user2.setUserPermissions(null);
		repository.save(user2);
		
		User user3 = new User();
		user3.setUsername("test-user3");
		user3.setCpf("511515155115");
		user3.setPhone("5115151511");
		user3.setProfessionalRegister("5151511511");
		user3.setUserPermissions(null);
		repository.save(user3);
		
		List<User> listUsers = repository.findAll();
		
		assertThat(listUsers).hasSize(3);
	}
	
	private User createUser() {
		User user = new User();
		user.setUsername("test-user");
		user.setCpf("511515155115");
		user.setPhone("5115151511");
		user.setProfessionalRegister("5151511511");
		user.setUserPermissions(null);
		
		repository.save(user);
		return user;
	}
}
