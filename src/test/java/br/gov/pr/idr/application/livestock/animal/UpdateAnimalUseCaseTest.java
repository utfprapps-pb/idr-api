package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.application.livestock.animal.update.UpdateAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.update.UpdateAnimalUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@DisplayName("UpdateAnimalUseCase")
class UpdateAnimalUseCaseTest {

    @Mock AnimalGateway animalGateway;
    @Mock BreedGateway breedGateway;
    @Mock AnimalAccessPolicy accessPolicy;
    UpdateAnimalUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateAnimalUseCase(animalGateway, new LivestockReferencePolicy(breedGateway, animalGateway),
                accessPolicy);
    }

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID breedId = UUID.randomUUID();
    private final Animal existingAnimal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    private UpdateAnimalCommand validCommand() {
        return new UpdateAnimalCommand(propertyId, animalId, "tecnico@idr.com", null, breedId, "T", "I", "F",
                "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    }

    @Test
    void shouldUpdateAnimalKeepingOriginalPropertyId() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new UpdateAnimalCommand(propertyId, animalId, "tecnico@idr.com",
                null, breedId, "T2", "I2", "M", "Morto", LocalDate.now(), 2f, 2f, 2f, 2f));

        assertEquals(propertyId, output.propertyId());
        assertEquals("I2", output.identifier());
    }

    @Test
    @DisplayName("PATCH parcial: campos ausentes mantêm o valor atual (form envia só identificador e raça)")
    void shouldKeepCurrentValuesForFieldsAbsentFromRequest() {
        final var motherId = AnimalID.unique();
        final var bornDate = LocalDate.of(2019, 6, 23);
        final var animalWithData = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), motherId,
                BreedID.unique(), "type 1", "Mimosa", "F", "Vivo", bornDate, 12f, 45f, 62f, 32f);
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animalWithData);
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new UpdateAnimalCommand(propertyId, animalId, "tecnico@idr.com",
                null, breedId, null, "Mimosa Editada", null, null, null, null, null, null, null));

        assertEquals("Mimosa Editada", output.identifier());
        assertEquals(breedId, output.breedId());
        assertEquals(motherId.id(), output.animalMotherId());
        assertEquals("type 1", output.type());
        assertEquals("F", output.gender());
        assertEquals("Vivo", output.bornCondition());
        assertEquals(bornDate, output.bornDate());
        assertEquals(12f, output.bornWeight());
        assertEquals(45f, output.previousWeight());
        assertEquals(62f, output.currentWeight());
        assertEquals(32f, output.ecc());
        verify(animalGateway, never()).existsByIdAndPropertyId(any(), any());
    }

    @Test
    void shouldThrowForbiddenWhenAccessGuardDenies() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(ForbiddenException.with("x"));
        assertThrows(ForbiddenException.class, () -> useCase.execute(validCommand()));
        verify(animalGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenBreedDoesNotExist() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(false);
        assertThrows(NotificationException.class, () -> useCase.execute(validCommand()));
    }

    @Test
    void shouldThrowWhenAnimalMotherIdDoesNotExist() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.existsByIdAndPropertyId(any(), any())).thenReturn(false);
        final var command = new UpdateAnimalCommand(propertyId, animalId, "tecnico@idr.com", UUID.randomUUID(),
                breedId, "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenAnimalMotherBelongsToDifferentProperty() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(true);

        final var motherId = UUID.randomUUID();
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(motherId), PropertyID.from(propertyId)))
                .thenReturn(false);

        final var command = new UpdateAnimalCommand(propertyId, animalId, "tecnico@idr.com", motherId,
                breedId, "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenAnimalMotherIsSelfReference() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(true);

        final var command = new UpdateAnimalCommand(propertyId, animalId, "tecnico@idr.com", animalId,
                breedId, "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(animalId), PropertyID.from(propertyId)))
                .thenReturn(true);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
        verify(animalGateway, never()).update(any());
    }
}
