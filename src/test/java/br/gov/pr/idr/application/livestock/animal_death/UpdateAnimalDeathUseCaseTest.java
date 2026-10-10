package br.gov.pr.idr.application.livestock.animal_death;

import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathGateway;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathID;
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
@DisplayName("UpdateAnimalDeathUseCase")
class UpdateAnimalDeathUseCaseTest {

    @Mock AnimalDeathGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks UpdateAnimalDeathUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID animalDeathId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final AnimalDeath existingAnimalDeath = AnimalDeath.with(
            AnimalDeathID.from(animalDeathId), AnimalID.from(animalId), "Picada de cobra", LocalDate.now());
    private final AnimalDeath animalDeathOfAnotherAnimal = AnimalDeath.with(
            AnimalDeathID.from(animalDeathId), AnimalID.unique(), "Picada de cobra", LocalDate.now());

    private UpdateAnimalDeathCommand validCommand() {
        return new UpdateAnimalDeathCommand(propertyId, animalId, animalDeathId, "tecnico@idr.com",
                "Timpanismo", LocalDate.of(2024, 4, 1));
    }

    @Test
    void shouldUpdateWhenAnimalDeathBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findById(AnimalDeathID.from(animalDeathId)))
                .thenReturn(Optional.of(existingAnimalDeath));
        when(gateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(validCommand());

        assertEquals("Timpanismo", output.reason());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalDeathBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findById(AnimalDeathID.from(animalDeathId)))
                .thenReturn(Optional.of(animalDeathOfAnotherAnimal));

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }
}
