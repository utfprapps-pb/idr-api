package br.gov.pr.idr.application.livestock.animal_death;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAnimalDeathUseCase")
class CreateAnimalDeathUseCaseTest {

    @Mock AnimalDeathGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks CreateAnimalDeathUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    private CreateAnimalDeathCommand validCreateCommand() {
        return new CreateAnimalDeathCommand(propertyId, animalId, PropertySearchScope.unrestricted(), "Picada de cobra",
                LocalDate.of(2024, 1, 1));
    }

    @Test
    void shouldCreateWhenAnimalIsAccessible() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(validCreateCommand());
        assertEquals("Picada de cobra", output.reason());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCreateCommand()));
        verify(gateway, never()).save(any());
    }

    @Test
    @DisplayName("um animal só morre uma vez: segundo registro de morte é rejeitado")
    void shouldRejectSecondDeathForSameAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.existsByAnimalId(AnimalID.from(animalId))).thenReturn(true);

        assertThrows(NotificationException.class, () -> useCase.execute(validCreateCommand()));
        verify(gateway, never()).save(any());
    }
}
