package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Test
    public void postProperty_whenPropertyIsValid_receiveCreated() {
        User user = userRepository.findById(1L).orElse(null);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, property, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postProperty_whenPropertyIsValid_propertySavedToDatabase() {
        User user = userRepository.findById(1L).orElse(null);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, property, Object.class);

        //Se compara com 4 pois existem três propriedades padrões inseridas no banco.
        assertThat( propertyRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteProperty_whenPropertyIdExists_receiveOk() {
        User user = userRepository.findById(1L).orElse(null);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity(API, property, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três propriedades padrões inseridas no banco.
        assertThat( propertyRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postProperty_whenPropertyIsValidAndAlreadyExists_propertyUpdateDatabase() {
        Property property = propertyRepository.findById(1L).orElse(null);
        property.setOcupationArea("Upadated Ocupation Area");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, property, Object.class);

        Property changedProperty = propertyRepository.findById(1L).orElse(null);
        assertThat(changedProperty.getOcupationArea()).isEqualTo("Upadated Ocupation Area");
    }

    @Test
    public void getProperty_whenPropertyExists_propertyReturnFromDatabase() {
        Property propertyDB = propertyRepository.findById(1L).orElse(null);

        List<Property> propertyList = propertyRepository.findAll();
        Property propertyDB1 = propertyList.get(0);

        assertThat(propertyDB).isEqualTo(propertyDB1);
    }

    private Property createValidProperty() {
        Property property = new Property();
        property.setOcupationArea("Ocupation Area");
        property.setLeased(true);

        return property;
    }
}
