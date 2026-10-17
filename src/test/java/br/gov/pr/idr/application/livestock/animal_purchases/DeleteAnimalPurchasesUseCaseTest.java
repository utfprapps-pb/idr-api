package br.gov.pr.idr.application.livestock.animal_purchases;

import br.gov.pr.idr.application.livestock.animal_purchases.delete.DeleteAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.delete.DeleteAnimalPurchasesUseCase;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteAnimalPurchasesUseCase")
class DeleteAnimalPurchasesUseCaseTest {

    @Mock AnimalPurchasesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks DeleteAnimalPurchasesUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID animalPurchasesId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final AnimalPurchases existingAnimalPurchases = AnimalPurchases.with(
            AnimalPurchasesID.from(animalPurchasesId), AnimalID.from(animalId), LocalDate.now(),
            LocalDate.now(), BigDecimal.valueOf(1500), BigDecimal.valueOf(450));
    private final AnimalPurchases animalPurchasesOfAnotherAnimal = AnimalPurchases.with(
            AnimalPurchasesID.from(animalPurchasesId), AnimalID.unique(), LocalDate.now(), LocalDate.now(),
            BigDecimal.valueOf(1500), BigDecimal.valueOf(450));

    private DeleteAnimalPurchasesCommand validCommand() {
        return new DeleteAnimalPurchasesCommand(propertyId, animalId, animalPurchasesId, "tecnico@idr.com");
    }

    @Test
    void shouldDeleteWhenAnimalPurchasesBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findById(AnimalPurchasesID.from(animalPurchasesId)))
                .thenReturn(Optional.of(existingAnimalPurchases));

        useCase.execute(validCommand());

        verify(gateway).deleteById(AnimalPurchasesID.from(animalPurchasesId));
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).deleteById(any());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalPurchasesBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findById(AnimalPurchasesID.from(animalPurchasesId)))
                .thenReturn(Optional.of(animalPurchasesOfAnotherAnimal));

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).deleteById(any());
    }
}
