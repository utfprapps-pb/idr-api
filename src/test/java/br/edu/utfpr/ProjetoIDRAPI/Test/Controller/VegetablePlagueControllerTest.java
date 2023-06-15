package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.utfpr.ProjetoIDRAPI.model.*;
import br.edu.utfpr.ProjetoIDRAPI.repository.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.repository.PlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.repository.VegetablePlagueRepository;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VegetablePlagueControllerTest {

	static final String API = "/vegetableplagues";

	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	private VegetablePlagueRepository vegetableRepository;

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private CultureRepository cultureRepository;

	@Autowired
	private PlagueRepository plagueRepository;

	@Test
	public void postVegetablePlague_whenVegetablePlagueIsValid_receiveCreated() {
		VegetablePlague vegetable = createValidVegetablePlague();
		vegetable.setPlague(plagueRepository.findById(1L).orElse(null));
		vegetable.setProperty(propertyRepository.findById(1L).orElse(null));
		vegetable.setCulture(cultureRepository.findById(1L).orElse(null));

		ResponseEntity<Object> response =
				testRestTemplate.postForEntity(API, vegetable, Object.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void postVegetablePlague_whenVegetablePlagueIsValid_vegetablePlagueSavedToDatabase() {
		VegetablePlague vegetable = createValidVegetablePlague();
		vegetable.setPlague(plagueRepository.findById(1L).orElse(null));
		vegetable.setProperty(propertyRepository.findById(1L).orElse(null));
		vegetable.setCulture(cultureRepository.findById(1L).orElse(null));

		ResponseEntity<Object> response =
				testRestTemplate.postForEntity(API, vegetable, Object.class);

		//Se compara com 4 pois existem três pragas vegetais padrões inseridas no banco.
		assertThat( vegetableRepository.count() ).isEqualTo(4);
	}

	@Test
	public void deleteVegetablePlague_whenVegetablePlagueIdExists_receiveOk() {
		VegetablePlague vegetable = createValidVegetablePlague();
		vegetable.setPlague(plagueRepository.findById(1L).orElse(null));
		vegetable.setProperty(propertyRepository.findById(1L).orElse(null));
		vegetable.setCulture(cultureRepository.findById(1L).orElse(null));

		ResponseEntity<Object> response =
				testRestTemplate.postForEntity(API, vegetable, Object.class);

		testRestTemplate.delete(API + "/4");

		//Se compara com 3 pois existem três pragas vegetais padrões inseridas no banco.
		assertThat( vegetableRepository.count() ).isEqualTo(3);
	}

	@Test
	public void postVegetablePlague_whenVegetablePlagueIsValidAndAlreadyExists_vegetablePlagueUpdateDatabase() {
		VegetablePlague vegetable = vegetableRepository.findById(1L).orElse(null);
		vegetable.setInfestationType("Changed Test Type");

		ResponseEntity<Object> response =
				testRestTemplate.postForEntity(API, vegetable, Object.class);

		VegetablePlague changedVegetable = vegetableRepository.findById(1L).orElse(null);
		assertThat(changedVegetable.getInfestationType()).isEqualTo("Changed Test Type");
	}

	@Test
	public void getVegetablePlague_whenVegetablePlagueExists_vegetablePlagueReturnFromDatabase() {
		VegetablePlague vegetableDB = vegetableRepository.findById(1L).orElse(null);

		List<VegetablePlague> vegetableList = vegetableRepository.findAll();
		VegetablePlague vegetableDB1 = vegetableList.get(0);

		assertThat(vegetableDB).isEqualTo(vegetableDB1);
	}
	
	private VegetablePlague createValidVegetablePlague() {
		VegetablePlague vegetablePlague = new VegetablePlague();
		vegetablePlague.setInfestationType("Test Type");
		
		return vegetablePlague;
	}
}