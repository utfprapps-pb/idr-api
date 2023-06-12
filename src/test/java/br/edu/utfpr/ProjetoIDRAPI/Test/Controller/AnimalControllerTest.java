package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalRepository;
import br.edu.utfpr.ProjetoIDRAPI.repository.BreedRepository;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyRepository;
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
public class AnimalControllerTest {

    private static final String API = "/animals";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    BreedRepository breedRepository;

    @Test
    public void postAnimal_whenAnimalIsValid_receiveCreated() {
        Animal animal = createValidAnimal();
        animal.setProperty(propertyRepository.findById(1L).orElse(null));
        animal.setBreed(breedRepository.findById(1L).orElse(null));
        animal.setAnimalMother(animalRepository.findById(1L).orElse(null));
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, animal, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postAnimal_whenAnimalIsValid_animalSavedToDatabase() {
        Animal animal = createValidAnimal();
        animal.setProperty(propertyRepository.findById(1L).orElse(null));
        animal.setBreed(breedRepository.findById(1L).orElse(null));
        animal.setAnimalMother(animalRepository.findById(1L).orElse(null));
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, animal, Object.class);

        //Se compara com 4 pois existem três animais padrões inseridos no banco.
        assertThat( animalRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteAnimal_whenAnimalIdExists_receiveOk() {
        Animal animal = createValidAnimal();
        animal.setProperty(propertyRepository.findById(1L).orElse(null));
        animal.setBreed(breedRepository.findById(1L).orElse(null));
        animal.setAnimalMother(animalRepository.findById(1L).orElse(null));
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, animal, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três animais padrões inseridos no banco.
        assertThat( animalRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postAnimal_whenAnimalIsValidAndAlreadyExists_animalUpdateDatabase() {
        Animal animal = animalRepository.findById(1L).orElse(null);
        animal.setIdentifier("changed identifier 1");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, animal, Object.class);

        Animal changedAnimal = animalRepository.findById(1L).orElse(null);
        assertThat(changedAnimal.getIdentifier()).isEqualTo("changed identifier 1");
    }

    @Test
    public void getAnimal_whenAnimalExists_animalReturnFromDatabase() {
        Animal animalDB = animalRepository.findById(1L).orElse(null);

        List<Animal> animalList = animalRepository.findAll();
        Animal animalDB1 = animalList.get(0);

        assertThat(animalDB).isEqualTo(animalDB1);
    }

    private Animal createValidAnimal() {
        Animal animal = new Animal();
        animal.setBornCondition("Vivo");
        animal.setIdentifier("Identifier-test");
        animal.setGender("M");

        return animal;
    }

}