package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

	static final String API = "/users";

    @Autowired
    TestRestTemplate testRestTemplate;

	@Autowired
	private UserRepository userRepository;

    @Test
    public void postUser_whenUserIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, user, Object.class);

        //Se compara com 4 pois existem três usuarios padrões inseridos no banco.
        assertThat( userRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteUser_whenUserIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, user, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três usuarios padrões inseridos no banco.
        assertThat( userRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postUser_whenUserIsValidAndAlreadyExists_userUpdateDatabase() {
        User user = userRepository.findById(1L).orElse(null);
        user.setDisplayName("User updated displayName");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, user, Object.class);

        User changedUser = userRepository.findById(1L).orElse(null);
        assertThat(changedUser.getDisplayName()).isEqualTo("User updated displayName");
    }

    @Test
    public void getUser_whenUserExists_userReturnFromDatabase() {
        User userDB = userRepository.findById(1L).orElse(null);

        List<User> userList = userRepository.findAll();
        User userDB1 = userList.get(0);

        assertThat(userDB).isEqualTo(userDB1);
    }
	
	private User createValidUser(){
		User user = new User();
		user.setUsername("User test");
        user.setDisplayName("User test displayName");
		user.setPassword("1717");
		user.setPhone("21221");
		user.setProfessionalRegister("22222");
		
		return user;
	}
}