package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.edu.utfpr.ProjetoIDRAPI.entity.city.City;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.RegionRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CityControllerTest {

    private static final String API = "/cities";

    @Autowired
    TestRestTemplate testRestTemplate;

	@Autowired
	private CityRepository cityRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    public void postCity_whenCityIsValid_receiveCreated() {
        Region region = regionRepository.findById(1L).orElse(null);

        City city = createValidCity();
        city.setCityRegion(region);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, city, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postCity_whenCityIsValid_citySavedToDatabase() {
        Region region = regionRepository.findById(1L).orElse(null);

        City city = createValidCity();
        city.setCityRegion(region);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, city, Object.class);

        assertThat( cityRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteCity_whenCityIdExists_receiveOk() {
        Region region = regionRepository.findById(1L).orElse(null);

        City city = createValidCity();
        city.setCityRegion(region);

        testRestTemplate.delete(API + "/1");

        assertThat( cityRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postCity_whenCityIsValidAndAlreadyExists_cityUpdateDatabase() {
        Region region = regionRepository.findById(1L).orElse(null);

        City city = createValidCity();
        city.setCityRegion(region);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, city, Object.class);

        City cityChange = cityRepository.findById(1L).orElse(null);
        cityChange.setName("Changed-test-city");
        ResponseEntity<Object> responseChanged =
                testRestTemplate.postForEntity(API, cityChange, Object.class);

        City changed = cityRepository.findById(1L).orElse(null);
        assertThat(changed.getName()).isEqualTo("Changed-test-city");
    }

    @Test
    public void getCity_whenCityExists_cityReturnFromDatabase() {
        Region region = regionRepository.findById(1L).orElse(null);

        City city = createValidCity();
        city.setCityRegion(region);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, city, Object.class);

        City cityDB = cityRepository.findById(1L).orElse(null);

        List<City> cityList = cityRepository.findAll();
        City cityDB1 = cityList.get(0);

        assertThat(cityDB).isEqualTo(cityDB1);
    }

	private City createValidCity() {
		City city = new City();
        city.setName("Test-city");

		return city;
	}

}
