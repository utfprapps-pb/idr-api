package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualFodders;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.PerennialAnualFoddersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PerennialAnualFoddersControllerTest {
    private static final String API = "/fodders";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PerennialAnualFoddersRepository perennialAnualFoddersRepository;

    @BeforeEach()
    private void cleanup() {
        perennialAnualFoddersRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postFodder_whenFodderIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualFodders perennialAnualFodders = new PerennialAnualFodders();
        perennialAnualFodders.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualFodders, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postFodder_whenFodderIsValid_fodderSavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualFodders perennialAnualFodders = new PerennialAnualFodders();
        perennialAnualFodders.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualFodders, Object.class);

        assertThat( perennialAnualFoddersRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteFodder_whenFodderIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualFodders perennialAnualFodders = new PerennialAnualFodders();
        perennialAnualFodders.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, perennialAnualFodders, Object.class);

        testRestTemplate.delete(API + "/1");

        assertThat( perennialAnualFoddersRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postFodder_whenFodderIsValidAndAlreadyExists_fodderUpdateDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualFodders perennialAnualFodders = new PerennialAnualFodders();
        perennialAnualFodders.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, perennialAnualFodders, Object.class);
        perennialAnualFodders.setId(1L);
        perennialAnualFodders.setFodder("Updated Test Fodder");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualFodders, Object.class);

        List<PerennialAnualFodders> fodderList = perennialAnualFoddersRepository.findAll();
        PerennialAnualFodders fodderDB = fodderList.get(0);
        assertThat(fodderDB.getFodder()).isEqualTo("Updated Test Fodder");
    }

    @Test
    public void getFodder_whenFodderExists_fodderReturnFromDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualFodders perennialAnualFodders = new PerennialAnualFodders();
        perennialAnualFodders.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, perennialAnualFodders, Object.class);
        perennialAnualFodders.setId(1L);

        PerennialAnualFodders fodderDB = perennialAnualFoddersRepository.findById(1L).orElse(null);

        List<PerennialAnualFodders> fodderList = perennialAnualFoddersRepository.findAll();
        PerennialAnualFodders fodderDB1 = fodderList.get(0);

        assertThat(fodderDB).isEqualTo(fodderDB1);
    }

    private User createValidUser() {
        User user = new User();
        user.setUsername("User-test-1");
        user.setCpf("115.675.888-66");
        user.setPhone("4632232277");
        user.setProfessionalRegister("999101");

        return user;
    }

    private Property createValidProperty() {
        Property property = new Property();
        property.setLeased(true);

        return property;
    }

    private PerennialAnualFodders createValidFodder() {
        PerennialAnualFodders perennialAnualFodders = new PerennialAnualFodders();
        perennialAnualFodders.setFodder("Test Fodder");
        perennialAnualFodders.setNote("Test Note");

        return perennialAnualFodders;
    }
}
