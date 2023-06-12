package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.repository.RegionRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegionControllerTest {

	static final String API = "/regions";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    RegionRepository regionRepository;

    @Test
    public void postRegion_whenRegionIsValid_receiveCreated() {
        Region region = createValidRegion();
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, region, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postRegion_whenRegionIsValid_regionSavedToDatabase() {
        Region region = createValidRegion();
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, region, Object.class);

        //Se compara com 4 pois existem três regiões padrões inseridas no banco.
        assertThat( regionRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteRegion_whenRegionIdExists_receiveOk() {
        Region region = createValidRegion();
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, region, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três regiões padrões inseridas no banco.
        assertThat( regionRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postRegion_whenRegionIsValidAndAlreadyExists_regionUpdateDatabase() {
        Region region = regionRepository.findById(1L).orElse(null);
        region.setName("Updated Test Region");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, region, Object.class);

        Region changedRegion = regionRepository.findById(1L).orElse(null);
        assertThat(changedRegion.getName()).isEqualTo("Updated Test Region");
    }

    @Test
    public void getRegion_whenRegionExists_regionReturnFromDatabase() {
        Region regionDB = regionRepository.findById(1L).orElse(null);

        List<Region> regionList = regionRepository.findAll();
        Region regionDB1 = regionList.get(0);

        assertThat(regionDB).isEqualTo(regionDB1);
    }
	
	private Region createValidRegion() {
		Region region = new Region();
        region.setName("Test Region");
		
		return region;
	}
}
