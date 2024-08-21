package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.DiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.VegetableDiseaseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VegetableDiseaseControllerTest {

	static final String API = "/vegetablediseases";

    @Autowired
    TestRestTemplate testRestTemplate;
	
	@Autowired
	private VegetableDiseaseRepository vegetableRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CultureRepository cultureRepository;

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Test
    public void postVegetableDisease_whenVegetableDiseaseIsValid_receiveCreated() {
        VegetableDisease vegetable = createValidVegetableDisease();
        vegetable.setDisease(diseaseRepository.findById(1L).orElse(null));
        vegetable.setProperty(propertyRepository.findById(1L).orElse(null));
        vegetable.setCulture(cultureRepository.findById(1L).orElse(null));

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, vegetable, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postVegetableDisease_whenVegetableDiseaseIsValid_vegetableDiseaseSavedToDatabase() {
        VegetableDisease vegetable = createValidVegetableDisease();
        vegetable.setDisease(diseaseRepository.findById(1L).orElse(null));
        vegetable.setProperty(propertyRepository.findById(1L).orElse(null));
        vegetable.setCulture(cultureRepository.findById(1L).orElse(null));

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, vegetable, Object.class);

        //Se compara com 4 pois existem três doenças vegetais padrões inseridas no banco.
        assertThat( vegetableRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteVegetableDisease_whenVegetableDiseaseIdExists_receiveOk() {
        VegetableDisease vegetable = createValidVegetableDisease();
        vegetable.setDisease(diseaseRepository.findById(1L).orElse(null));
        vegetable.setProperty(propertyRepository.findById(1L).orElse(null));
        vegetable.setCulture(cultureRepository.findById(1L).orElse(null));

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, vegetable, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três doenças vegetais padrões inseridas no banco.
        assertThat( vegetableRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postVegetablePlague_whenVegetablePlagueIsValidAndAlreadyExists_vegetablePlagueUpdateDatabase() {
        VegetableDisease vegetable = vegetableRepository.findById(1L).orElse(null);
        vegetable.setInfestationType("Changed Test Type");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, vegetable, Object.class);

        VegetableDisease changedVegetable = vegetableRepository.findById(1L).orElse(null);
        assertThat(changedVegetable.getInfestationType()).isEqualTo("Changed Test Type");
    }

    @Test
    public void getVegetablePlague_whenVegetablePlagueExists_vegetablePlagueReturnFromDatabase() {
        VegetableDisease vegetableDB = vegetableRepository.findById(1L).orElse(null);

        List<VegetableDisease> vegetableList = vegetableRepository.findAll();
        VegetableDisease vegetableDB1 = vegetableList.get(0);

        assertThat(vegetableDB).isEqualTo(vegetableDB1);
    }
	
	private VegetableDisease createValidVegetableDisease() {
		VegetableDisease vegetableDisease = new VegetableDisease();
		vegetableDisease.setInfestationType("Test Type");
		
		return vegetableDisease;
	}
}