package br.edu.utfpr.ProjetoIDRAPI.Test.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserRepositoryTest {
	@Autowired
	private UserRepository repository;
	
	private User user;
	
	@BeforeEach
	public void setup() {
		user = User.builder().id(1l).username("user-test").cpf("115151155").phone("15515115").professionalRegister("1515").build();	
	}
	
	@Test
	public void whenFindAll_thenReturnsList() {
		User us = User.builder().id(2l).username("user-test-list").cpf("6565565").phone("6626262").professionalRegister("959").build();
		
		repository.save(user);
		repository.save(us);
		
		List<User> userList = repository.findAll();
		
		assertThat(userList).isNotNull();
		assertThat(userList.size()).isEqualTo(2);
	}
	
	@Test
	public void whenFindById_returnRegionObject() {
		repository.save(user);
		
		User userFind = repository.findById(user.getId()).get();
		
		assertThat(userFind).isNotNull();
	}
	
	@Test
	public void whenFindByName_returnRegionObject() {
		User userFind = repository.findByusername(user.getUsername());
		System.out.println(userFind);
		
		assertThat(userFind).isNotNull();
	}
}
