package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyCollaboratorRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyCollaboratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PropertyCollaboratorControllerTest {
    private static final String API = "/propertyCollaborators";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PropertyCollaboratorRepository collaboratorRepository;

    @BeforeEach()
    private void cleanup() {
        collaboratorRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postProductUse_whenProductUseIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postProductUse_whenProductUseIsValid_productUseSavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        assertThat( collaboratorRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteProductUse_whenProductUseIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        testRestTemplate.delete(API + "/1");

        assertThat( collaboratorRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postProductUse_whenProductUseIsValidAndAlreadyExists_productUseUpdateDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);


        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, collaborator, Object.class);
        collaborator.setId(1L);
        collaborator.setCollaboratorName("Updated Josias");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        List<PropertyCollaborator> collaboratorList = collaboratorRepository.findAll();
        PropertyCollaborator collaboratorDB = collaboratorList.get(0);
        assertThat(collaboratorDB.getCollaboratorName()).isEqualTo("Updated Josias");
    }

    @Test
    public void getProductUse_whenProductUseExists_productUseReturnFromDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, collaborator, Object.class);
        collaborator.setId(1L);

        PropertyCollaborator collaboratorDB = collaboratorRepository.findById(1L).orElse(null);

        List<PropertyCollaborator> collaboratorList = collaboratorRepository.findAll();
        PropertyCollaborator collaboratorDB1 = collaboratorList.get(0);

        assertThat(collaboratorDB).isEqualTo(collaboratorDB1);
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

    private PropertyCollaborator createValidCollaborator() {
        PropertyCollaborator collaborator = new PropertyCollaborator();
        collaborator.setCollaboratorName("Josias");
        collaborator.setWorkHours(12);
        collaborator.setWorkDays(7);

        return collaborator;
    }

}
