package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAnimalByIdUseCase")
class GetAnimalByIdUseCaseTest {

    @Mock AnimalAccessPolicy accessPolicy;
    @Mock LivestockNameLookup nameLookup;
    @InjectMocks GetAnimalByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final BreedID breedId = BreedID.unique();
    private final Animal existingAnimal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            breedId, null, "Mimosa", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    @Test
    void shouldReturnAnimalWhenAuthorized() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(existingAnimal);
        when(nameLookup.breedName(breedId)).thenReturn("Holandesa");

        final var output = useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, PropertySearchScope.unrestricted()));

        assertEquals("Mimosa", output.identifier());
        assertEquals("Holandesa", output.breedName());
    }

    @Test
    void shouldReturnSizeName() {
        final var sizeId = AnimalSizeID.unique();
        final var animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null, breedId,
                sizeId, "Mimosa", null, null, null, null, null, null, null);
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(nameLookup.sizeName(sizeId)).thenReturn("Grande");

        final var output = useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, PropertySearchScope.unrestricted()));

        assertEquals(sizeId.id(), output.sizeId());
        assertEquals("Grande", output.sizeName());
    }

    @Test
    void shouldReturnNullSizeWithoutSize() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(existingAnimal);

        final var output = useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, PropertySearchScope.unrestricted()));

        assertNull(output.sizeId());
        assertNull(output.sizeName());
        verify(nameLookup, never()).sizeName(any());
    }

    @Test
    void shouldReturnMotherIdentifier() {
        final var motherId = AnimalID.unique();
        final var animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), motherId, breedId,
                null, "Charmosa", null, null, null, null, null, null, null);
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(nameLookup.animalIdentifier(motherId)).thenReturn("Mimosa");

        final var output = useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, PropertySearchScope.unrestricted()));

        assertEquals(motherId.id(), output.animalMotherId());
        assertEquals("Mimosa", output.animalMotherIdentifier());
    }

    @Test
    void shouldReturnNullMotherIdentifierWithoutMother() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(existingAnimal);

        final var output = useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, PropertySearchScope.unrestricted()));

        assertNull(output.animalMotherIdentifier());
        verify(nameLookup, never()).animalIdentifier(any());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalBelongsToAnotherProperty() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted()))
                .thenThrow(NotFoundException.with("Animal com o ID %s não encontrado".formatted(animalId)));

        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, PropertySearchScope.unrestricted())));
    }
}
