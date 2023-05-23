package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

/*import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.model.Breed;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalRepository;
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
@ActiveProfiles("test")*/
public class AnimalControllerTest {
    /*private static final String API = "/animals";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AnimalRepository animalRepository;

    @BeforeEach()
    private void cleanup() {
        animalRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postAnimal_whenAnimalIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        Animal animal = createValidAnimal();
        animal.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, animal, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postAnimal_whenAnimalIsValid_animalSavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        Animal animal = createValidAnimal();
        animal.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, animal, Object.class);

        assertThat( animalRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteAnimal_whenAnimalIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        Animal animal = createValidAnimal();
        animal.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, animal, Object.class);

        testRestTemplate.delete(API + "/1");

        assertThat( animalRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postAnimal_whenAnimalIsValidAndAlreadyExists_animalUpdateDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);
        
        Breed breed = new Breed();
		breed.setBreedName("Holandês");

        Animal animal = createValidAnimal();
        animal.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, animal, Object.class);
        animal.setId(1L);
        animal.setBreed(breed);

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, animal, Object.class);

        List<Animal> animalList = animalRepository.findAll();
        Animal animalDB = animalList.get(0);
        assertThat(animalDB.getBreed()).isEqualTo("Updated Vaca vaca");
    }

    @Test
    public void getAnimal_whenAnimalExists_animalReturnFromDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        Animal animal = createValidAnimal();
        animal.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, animal, Object.class);
        animal.setId(1L);

        Animal animalDB = animalRepository.findById(1L).orElse(null);

        List<Animal> animalList = animalRepository.findAll();
        Animal animalDB1 = animalList.get(0);

        assertThat(animalDB).isEqualTo(animalDB1);
    }

    private User createValidUser() {
        User user = new User();
        user.setUsername("User-test-1");
        user.setPassword("115.675.888-66");
        user.setPhone("4632232277");
        user.setProfessionalRegister("999101");

        return user;
    }

    private Property createValidProperty() {
        Property property = new Property();
        property.setLeased(true);

        return property;
    }

    private Animal createValidAnimal() {
    	Breed breed = new Breed();
		breed.setBreedName("Girolando");
    	
        Animal animal = new Animal();
        animal.setBreed(breed);

        return animal;
    }*/
}