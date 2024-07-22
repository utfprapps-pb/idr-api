package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.perennialAnualForage.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialAnualForage.PerennialAnualForageRepository;
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
public class PerennialAnualForageControllerTest {

    private static final String API = "/fodders";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PerennialAnualForageRepository perennialAnualForageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    public void postForage_whenForageIsValid_receiveCreated() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postForage_whenForageIsValid_forageSavedToDatabase() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        //Se compara com 4 pois existem três foragens padrões inseridas no banco.
        assertThat( perennialAnualForageRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteForage_whenForageIdExists_receiveOk() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PerennialAnualForage perennialAnualForage = createValidForage();
        perennialAnualForage.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três foragens padrões inseridas no banco.
        assertThat( perennialAnualForageRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postForage_whenForageIsValidAndAlreadyExists_forageUpdateDatabase() {
        PerennialAnualForage perennialAnualForage = perennialAnualForageRepository.findById(1L).orElse(null);
        perennialAnualForage.setForage("Updated Test Fodder");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, perennialAnualForage, Object.class);

        PerennialAnualForage changedFodder = perennialAnualForageRepository.findById(1L).orElse(null);
        assertThat(changedFodder.getForage()).isEqualTo("Updated Test Fodder");
    }

    @Test
    public void getForage_whenForageExists_forageReturnFromDatabase() {
        PerennialAnualForage forageDB = perennialAnualForageRepository.findById(1L).orElse(null);

        List<PerennialAnualForage> forageList = perennialAnualForageRepository.findAll();
        PerennialAnualForage forageDB1 = forageList.get(0);

        assertThat(forageDB).isEqualTo(forageDB1);
    }

    private PerennialAnualForage createValidForage() {
        PerennialAnualForage perennialAnualForage = new PerennialAnualForage();
        perennialAnualForage.setForage("Test Forage");
        perennialAnualForage.setNote("Test Note");

        return perennialAnualForage;
    }
}