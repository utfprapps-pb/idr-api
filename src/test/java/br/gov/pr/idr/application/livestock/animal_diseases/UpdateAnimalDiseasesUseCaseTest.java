package br.gov.pr.idr.application.livestock.animal_diseases;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesUseCase;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateAnimalDiseasesUseCase")
class UpdateAnimalDiseasesUseCaseTest {

    @Mock AnimalDiseasesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks UpdateAnimalDiseasesUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID animalDiseasesId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final AnimalDiseases existingAnimalDiseases = AnimalDiseases.with(
            AnimalDiseasesID.from(animalDiseasesId), AnimalID.from(animalId), "Febre aftosa", LocalDate.now());

    private UpdateAnimalDiseasesCommand validCommand() {
        return new UpdateAnimalDiseasesCommand(propertyId, animalId, animalDiseasesId, PropertySearchScope.unrestricted(),
                "Brucelose", LocalDate.of(2024, 4, 1));
    }

    @Test
    void shouldUpdateWhenAnimalDiseasesBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalDiseasesID.from(animalDiseasesId), AnimalID.from(animalId)))
                .thenReturn(Optional.of(existingAnimalDiseases));
        when(gateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(validCommand());

        assertEquals("Brucelose", output.diagnosis());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalDiseasesBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalDiseasesID.from(animalDiseasesId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }
}
