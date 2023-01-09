package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyEquipImproveRepository;
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
public class PropertyEquipImproveControllerTest {
    private static final String API = "/propertyEquipImproves";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private PropertyEquipImproveRepository equipImproveRepository;

    @BeforeEach()
    private void cleanup() {
        equipImproveRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postEquipImprove_whenEquipImproveIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postEquipImprove_whenEquipImproveIsValid_equipImproveSavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        assertThat( equipImproveRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteEquipImprove_whenEquipImproveIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        testRestTemplate.delete(API + "/1");

        assertThat( equipImproveRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postEquipImprove_whenEquipImproveIsValidAndAlreadyExists_equipImproveUpdateDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);
        equipImprove.setId(1L);
        equipImprove.setType("Updated test type");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        List<PropertyEquipImprove> equipImproveList = equipImproveRepository.findAll();
        PropertyEquipImprove equipImproveDB = equipImproveList.get(0);
        assertThat(equipImproveDB.getType()).isEqualTo("Updated test type");
    }

    @Test
    public void getEquipImprove_whenEquipImproveExists_equipImproveReturnFromDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);
        equipImprove.setId(1L);

        PropertyEquipImprove equipImproveDB = equipImproveRepository.findById(1L).orElse(null);

        List<PropertyEquipImprove> equipImproveList = equipImproveRepository.findAll();
        PropertyEquipImprove equipImproveDB1 = equipImproveList.get(0);

        assertThat(equipImproveDB).isEqualTo(equipImproveDB1);
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

    private PropertyEquipImprove createValidEquip() {
        PropertyEquipImprove equipImprove = new PropertyEquipImprove();
        equipImprove.setType("Test type");
        equipImprove.setName("Equipament test");

        return equipImprove;
    }
}
