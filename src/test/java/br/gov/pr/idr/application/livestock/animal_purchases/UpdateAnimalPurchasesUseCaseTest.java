package br.gov.pr.idr.application.livestock.animal_purchases;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
@DisplayName("UpdateAnimalPurchasesUseCase")
class UpdateAnimalPurchasesUseCaseTest {

    @Mock AnimalPurchasesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks UpdateAnimalPurchasesUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID animalPurchasesId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final AnimalPurchases existingAnimalPurchases = AnimalPurchases.with(
            AnimalPurchasesID.from(animalPurchasesId), AnimalID.from(animalId), LocalDate.now(),
            LocalDate.now(), BigDecimal.valueOf(1500), BigDecimal.valueOf(450));

    private UpdateAnimalPurchasesCommand validCommand() {
        return new UpdateAnimalPurchasesCommand(propertyId, animalId, animalPurchasesId, PropertySearchScope.unrestricted(),
                LocalDate.of(2024, 4, 1), LocalDate.of(2023, 4, 1), BigDecimal.valueOf(2000), BigDecimal.valueOf(480));
    }

    @Test
    void shouldUpdateWhenAnimalPurchasesBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalPurchasesID.from(animalPurchasesId), AnimalID.from(animalId)))
                .thenReturn(Optional.of(existingAnimalPurchases));
        when(gateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(validCommand());

        assertEquals(BigDecimal.valueOf(2000), output.value());
        assertEquals(BigDecimal.valueOf(480), output.weight());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalPurchasesBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalPurchasesID.from(animalPurchasesId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }
}
