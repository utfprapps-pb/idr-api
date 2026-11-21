package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
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
import static org.junit.jupiter.api.Assertions.assertNull;
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
        return new UpdateAnimalCommand(propertyId, animalId, PropertySearchScope.unrestricted(), null, breedId, "T", "I", "F",
                "Vivo", LocalDate.now(), 1f);
    }

    @Test
    void shouldUpdateAnimalKeepingOriginalPropertyId() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new UpdateAnimalCommand(propertyId, animalId, PropertySearchScope.unrestricted(),
                null, breedId, "T2", "I2", "M", "Morto", LocalDate.now(), 2f));

        assertEquals(propertyId, output.propertyId());
        assertEquals("I2", output.identifier());
    }

    private Animal animalWithData(final AnimalID motherId) {
        return Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), motherId, BreedID.unique(),
                "Grande", "Mimosa", "Fêmea", "Vivo", LocalDate.of(2019, 6, 23), 12f, 45f, 62f, 3.5f);
    }

    @Test
    @DisplayName("PUT: campo opcional enviado nulo é apagado, inclusive a mãe")
    void shouldClearOptionalFieldsSentAsNull() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted()))
                .thenReturn(animalWithData(AnimalID.unique()));
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new UpdateAnimalCommand(propertyId, animalId,
                PropertySearchScope.unrestricted(), null, breedId, null, "Mimosa", null, null, null, null));

        assertNull(output.animalMotherId());
        assertNull(output.type());
        assertNull(output.gender());
        assertNull(output.bornCondition());
        assertNull(output.bornDate());
        assertNull(output.bornWeight());
        verify(animalGateway, never()).existsByIdAndPropertyId(any(), any());
    }

    @Test
    @DisplayName("PUT: peso anterior, peso atual e ECC são preservados")
    void shouldPreserveMeasuresOnUpdate() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted()))
                .thenReturn(animalWithData(null));
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(validCommand());

        assertEquals(45f, output.previousWeight());
        assertEquals(62f, output.currentWeight());
        assertEquals(3.5f, output.ecc());
    }

    @Test
    @DisplayName("PUT: campos de identificação são substituídos pelos enviados")
    void shouldReplaceIdentificationFields() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted()))
                .thenReturn(animalWithData(null));
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));
        final var bornDate = LocalDate.of(2020, 1, 31);

        final var output = useCase.execute(new UpdateAnimalCommand(propertyId, animalId,
                PropertySearchScope.unrestricted(), null, breedId, "Média", "Estrela", "Macho", "Morto", bornDate,
                30f));

        assertEquals(breedId, output.breedId());
        assertEquals("Média", output.type());
        assertEquals("Estrela", output.identifier());
        assertEquals("Macho", output.gender());
        assertEquals("Morto", output.bornCondition());
        assertEquals(bornDate, output.bornDate());
        assertEquals(30f, output.bornWeight());
    }

    @Test
    @DisplayName("PUT: mãe inalterada não é revalidada (permite editar filho de mãe excluída)")
    void shouldNotRevalidateUnchangedMother() {
        final var motherId = AnimalID.unique();
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted()))
                .thenReturn(animalWithData(motherId));
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new UpdateAnimalCommand(propertyId, animalId,
                PropertySearchScope.unrestricted(), motherId.id(), breedId, null, "Mimosa Editada", null, null,
                null, null));

        assertEquals(motherId.id(), output.animalMotherId());
        assertEquals("Mimosa Editada", output.identifier());
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
        final var command = new UpdateAnimalCommand(propertyId, animalId, PropertySearchScope.unrestricted(), UUID.randomUUID(),
                breedId, "T", "I", "F", "Vivo", LocalDate.now(), 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenAnimalMotherBelongsToDifferentProperty() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(true);

        final var motherId = UUID.randomUUID();
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(motherId), PropertyID.from(propertyId)))
                .thenReturn(false);

        final var command = new UpdateAnimalCommand(propertyId, animalId, PropertySearchScope.unrestricted(), motherId,
                breedId, "T", "I", "F", "Vivo", LocalDate.now(), 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenAnimalMotherIsSelfReference() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(existingAnimal);
        when(breedGateway.existsById(any())).thenReturn(true);

        final var command = new UpdateAnimalCommand(propertyId, animalId, PropertySearchScope.unrestricted(), animalId,
                breedId, "T", "I", "F", "Vivo", LocalDate.now(), 1f);
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(animalId), PropertyID.from(propertyId)))
                .thenReturn(true);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
        verify(animalGateway, never()).update(any());
    }
}
