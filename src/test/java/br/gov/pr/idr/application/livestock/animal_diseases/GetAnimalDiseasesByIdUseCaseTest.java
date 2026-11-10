package br.gov.pr.idr.application.livestock.animal_diseases;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesGateway;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAnimalDiseasesByIdUseCase")
class GetAnimalDiseasesByIdUseCaseTest {

    @Mock AnimalDiseasesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks GetAnimalDiseasesByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID animalDiseasesId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final AnimalDiseases existingAnimalDiseases = AnimalDiseases.with(
            AnimalDiseasesID.from(animalDiseasesId), AnimalID.from(animalId), "Febre aftosa",
            LocalDate.of(2024, 1, 1));

    @Test
    void shouldReturnAnimalDiseasesWhenItBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalDiseasesID.from(animalDiseasesId), AnimalID.from(animalId)))
                .thenReturn(Optional.of(existingAnimalDiseases));

        final var output = useCase.execute(new GetAnimalDiseasesByIdCommand(propertyId, animalId,
                animalDiseasesId, PropertySearchScope.unrestricted()));

        assertEquals("Febre aftosa", output.diagnosis());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalDiseasesBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalDiseasesID.from(animalDiseasesId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetAnimalDiseasesByIdCommand(propertyId, animalId,
                        animalDiseasesId, PropertySearchScope.unrestricted())));
    }
}
