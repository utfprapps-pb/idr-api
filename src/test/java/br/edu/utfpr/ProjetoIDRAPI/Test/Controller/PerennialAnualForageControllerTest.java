package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.PerennialAnualForageRepository;
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
public class PerennialAnualForageControllerTest {
    private static final String API = "/fodders";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PerennialAnualForageRepository perennialAnualForageRepository;

    @BeforeEach()
    private void cleanup() {
        perennialAnualForageRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postForage_whenForageIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postForage_whenForageIsValid_forageSavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        assertThat( perennialAnualForageRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteForage_whenForageIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        testRestTemplate.delete(API + "/1");

        assertThat( perennialAnualForageRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postForage_whenForageIsValidAndAlreadyExists_forageUpdateDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);
        perennialAnualForage.setId(1L);
        perennialAnualForage.setForage("Updated Test Fodder");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        List<PerennialAnualForage> fodderList = perennialAnualForageRepository.findAll();
        PerennialAnualForage fodderDB = fodderList.get(0);
        assertThat(fodderDB.getForage()).isEqualTo("Updated Test Fodder");
    }

    @Test
    public void getForage_whenForageExists_forageReturnFromDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);
        perennialAnualForage.setId(1L);

        PerennialAnualForage forageDB = perennialAnualForageRepository.findById(1L).orElse(null);

        List<PerennialAnualForage> forageList = perennialAnualForageRepository.findAll();
        PerennialAnualForage forageDB1 = forageList.get(0);

        assertThat(forageDB).isEqualTo(forageDB1);
    }

    private User createValidUser() {
        User user = new User();
        user.setUsername("User-test-1");
        user.setPassword("115.675.888-66");
        user.setPhone("4632232277");
        user.setProfessionalRegister("999101");

        return user;
    }

    private Property createValidProperty() {
        Property property = new Property();
        property.setLeased(true);

        return property;
    }

    private PerennialAnualForage createValidForage() {
        PerennialAnualForage perennialAnualForage = new PerennialAnualForage();
        perennialAnualForage.setForage("Test Forage");
        perennialAnualForage.setNote("Test Note");

        return perennialAnualForage;
    }
}