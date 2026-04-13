package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.AnimalRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto.AnimalDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.breed.Breed;
import br.edu.utfpr.ProjetoIDRAPI.entity.breed.BreedRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AnimalControllerTest extends CrudControllerTest<Animal, AnimalDto, Long> {

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private BreedRepository breedRepository;
    @Autowired
    private UserRepository userRepository;

    private Breed savedBreed;
    private Property savedProperty;

    @BeforeEach
    void setup() {
        // Persiste as dependências antes de cada teste
        User owner = TestUtils.createValidUser("Owner");
        owner = userRepository.save(owner);

        User techUser = TestUtils.createValidUser("Tech");
        techUser = userRepository.save(techUser);

        PropertyTechnician technician = TestUtils.createValidPropertyTechnician(techUser);
        savedProperty = TestUtils.createValidProperty(owner, List.of(technician));
        propertyRepository.save(savedProperty);

        savedBreed = TestUtils.createValidBreed();
        breedRepository.save(savedBreed);
    }

    @Override
    protected Long persistAndReturnId(Animal entity) {
        // A entidade já vem com as dependências salvas, então basta salvá-la
        return animalRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        // A ordem de limpeza deve ser o inverso da criação para respeitar as constraints
        animalRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
        breedRepository.deleteAll();
    }

    @Override
    protected Animal createValidObject() {
        // Usa as dependências já salvas no setup
        return TestUtils.createValidAnimal(savedProperty, savedBreed);
    }

    @Override
    protected Animal createInvalidObject() {
        return new Animal();
    }

    @Override
    protected String getURL() {
        return "/animals";
    }

    @Override
    protected Class<AnimalDto> getDtoClass() {
        return AnimalDto.class;
    }
}