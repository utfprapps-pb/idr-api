package br.gov.pr.idr.application.livestock.animal_sales;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesGateway;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesID;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;
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
@DisplayName("UpdateAnimalSalesUseCase")
class UpdateAnimalSalesUseCaseTest {

    @Mock AnimalSalesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks UpdateAnimalSalesUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID animalSalesId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final AnimalSales existingAnimalSales = AnimalSales.with(
            AnimalSalesID.from(animalSalesId), AnimalID.from(animalId), LocalDate.now(),
            Reason.VOLUNTARIA, BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450));

    private UpdateAnimalSalesCommand validCommand() {
        return new UpdateAnimalSalesCommand(propertyId, animalId, animalSalesId, PropertySearchScope.unrestricted(),
                LocalDate.of(2024, 4, 1), Reason.EMERGENCIA, BigDecimal.valueOf(2000), Destination.PRODUCAO, BigDecimal.valueOf(480));
    }

    @Test
    void shouldUpdateWhenAnimalSalesBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalSalesID.from(animalSalesId), AnimalID.from(animalId)))
                .thenReturn(Optional.of(existingAnimalSales));
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
    void shouldThrowNotFoundWhenAnimalSalesBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalSalesID.from(animalSalesId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }
}
