package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.forageDisponibility.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.entity.forageDisponibility.ForageDisponibilityRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ForageDisponibilityControllerTest {

	private static final String API = "/foragesDisponibilities";

    @Autowired
    TestRestTemplate testRestTemplate;
	
	@Autowired
	private ForageDisponibilityRepository forageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    public void postForage_whenForageIsValid_receiveCreated() {
        Property property = propertyRepository.findById(1L).orElse(null);

        ForageDisponibility forage = createValidForageDisponibility();
        forage.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, forage, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postAnimal_whenAnimalIsValid_animalSavedToDatabase() {
        Property property = propertyRepository.findById(1L).orElse(null);

        ForageDisponibility forage = createValidForageDisponibility();
        forage.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, forage, Object.class);

        //Se compara com 4 pois existem três forragens padrões inseridas no banco.
        assertThat( forageRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteAnimal_whenAnimalIdExists_receiveOk() {
        Property property = propertyRepository.findById(1L).orElse(null);

        ForageDisponibility forage = createValidForageDisponibility();
        forage.setProperty(property);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três forragens padrões inseridas no banco.
        assertThat( forageRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postAnimal_whenAnimalIsValidAndAlreadyExists_animalUpdateDatabase() {
        ForageDisponibility forage = forageRepository.findById(1L).orElse(null);
        forage.setForage("Changed-forage-test");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, forage, Object.class);

        ForageDisponibility changedForage = forageRepository.findById(1L).orElse(null);
        assertThat(changedForage.getForage()).isEqualTo("Changed-forage-test");
    }

    @Test
    public void getAnimal_whenAnimalExists_animalReturnFromDatabase() {
        ForageDisponibility forageDB = forageRepository.findById(1L).orElse(null);

        List<ForageDisponibility> forageList = forageRepository.findAll();
        ForageDisponibility forageDB1 = forageList.get(0);

        assertThat(forageDB).isEqualTo(forageDB1);
    }

	private ForageDisponibility createValidForageDisponibility() {
		ForageDisponibility forageDisponibility = new ForageDisponibility();
		forageDisponibility.setForage("Forage-test");
		
		return forageDisponibility;
	}

}