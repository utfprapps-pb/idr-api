package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyEquipImprove.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyEquipImprove.PropertyEquipImproveRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
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
    PropertyEquipImproveRepository equipImproveRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Test
    public void postEquipImprove_whenEquipImproveIsValid_receiveCreated() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postEquipImprove_whenEquipImproveIsValid_equipImproveSavedToDatabase() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        //Se compara com 4 pois existem três equipamentos padrões inseridos no banco.
        assertThat( equipImproveRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteEquipImprove_whenEquipImproveIdExists_receiveOk() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PropertyEquipImprove equipImprove = createValidEquip();
        equipImprove.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três equipamentos padrões inseridos no banco.
        assertThat( equipImproveRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postEquipImprove_whenEquipImproveIsValidAndAlreadyExists_equipImproveUpdateDatabase() {
        PropertyEquipImprove equipImprove = equipImproveRepository.findById(1L).orElse(null);
        equipImprove.setType("Updated test type");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, equipImprove, Object.class);

        PropertyEquipImprove changedEquipImprove = equipImproveRepository.findById(1L).orElse(null);
        assertThat(changedEquipImprove.getType()).isEqualTo("Updated test type");
    }

    @Test
    public void getEquipImprove_whenEquipImproveExists_equipImproveReturnFromDatabase() {
        PropertyEquipImprove equipImproveDB = equipImproveRepository.findById(1L).orElse(null);

        List<PropertyEquipImprove> equipImproveList = equipImproveRepository.findAll();
        PropertyEquipImprove equipImproveDB1 = equipImproveList.get(0);

        assertThat(equipImproveDB).isEqualTo(equipImproveDB1);
    }

    private PropertyEquipImprove createValidEquip() {
        PropertyEquipImprove equipImprove = new PropertyEquipImprove();
        equipImprove.setType("Test type");
        equipImprove.setName("Equipament test");

        return equipImprove;
    }
}