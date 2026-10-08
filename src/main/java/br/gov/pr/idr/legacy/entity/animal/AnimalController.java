package br.gov.pr.idr.legacy.entity.animal;

import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.gov.pr.idr.legacy.entity.animal.dto.AnimalDto;
import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.utils.GenericResponse;
import jakarta.validation.Valid;

@RestController
// Gambiarra, deve ser refatorado pra haver restrição de busca do animal para
// cada propriedade, seguindo a documentação do SpringBoot:
// https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
@RequestMapping("properties/*/animals")
public class AnimalController extends CrudController<Animal, AnimalDto, UUID> {

    private final AnimalService animalService;
    private final ModelMapper modelMapper;

    public AnimalController(AnimalService animalService, ModelMapper modelMapper) {
        super(Animal.class, AnimalDto.class);
        this.animalService = animalService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<Animal, UUID> getService() {
        return this.animalService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @PatchMapping("{id}")
    @Override
    public ResponseEntity<AnimalDto> update(@RequestBody @Valid AnimalDto dto, @PathVariable UUID id) {
        Animal entity = getModelMapper().map(dto, Animal.class);
        Animal updatedEntity = animalService.update(id, entity);
        return ResponseEntity.ok(convertEntityToDto(updatedEntity));
    }

    @GetMapping("/findAnimal/{identifier}")
    public ResponseEntity<AnimalDto> findAnimalByIdentifier(@PathVariable String identifier) {
        Animal entity = animalService.findByIdentifier(identifier);
        
        if(entity != null) {
        	return ResponseEntity.ok(convertEntityToDto(animalService.findByIdentifier(identifier)));
        } else {
    		return ResponseEntity.noContent().build();
    	}
    }

    @PostMapping("/sendAnimal")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createRegister(@RequestBody @Valid Animal animal) {
        animalService.save(animal);
        return new GenericResponse("Registro inserido com sucesso");
    }

    @PostMapping("/sendAnimals")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createListRegister(@RequestBody @Valid List<Animal> animals) {
        animalService.saveListAnimals(animals);
        return new GenericResponse("Registros inseridos com sucesso");
    }

    private AnimalDto convertEntityToDto(Animal animal) {
        return modelMapper.map(animal, AnimalDto.class);
    }
}
