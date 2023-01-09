package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyRepository;
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
public class PropertyControllerTest {
    private static final String API = "/properties";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PropertyRepository propertyRepository;

    @BeforeEach()
    private void cleanup() {
        propertyRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postProperty_whenPropertyIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, property, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postProperty_whenPropertyIsValid_propertySavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, property, Object.class);

        assertThat( propertyRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteProperty_whenPropertyIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity(API, property, Object.class);

        testRestTemplate.delete(API + "/1");

        assertThat( propertyRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postProperty_whenPropertyIsValidAndAlreadyExists_propertyUpdateDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity(API, property, Object.class);
        property.setId(1L);
        property.setOcupationArea("Upadated Ocupation Area");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, property, Object.class);

        List<Property> propertyList = propertyRepository.findAll();
        Property propertyDB = propertyList.get(0);
        assertThat(propertyDB.getOcupationArea()).isEqualTo("Upadated Ocupation Area");
    }

    @Test
    public void getProperty_whenPropertyExists_propertyReturnFromDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity(API, property, Object.class);
        property.setId(1L);

        Property propertyDB = propertyRepository.findById(1L).orElse(null);

        List<Property> propertyList = propertyRepository.findAll();
        Property propertyDB1 = propertyList.get(0);

        assertThat(propertyDB).isEqualTo(propertyDB1);
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
        property.setOcupationArea("Ocupation Area");
        property.setLeased(true);

        return property;
    }

}
