package br.gov.pr.idr.application.livestock.animal_sales;

import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdUseCase;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAnimalSalesByIdUseCase")
class GetAnimalSalesByIdUseCaseTest {

    @Mock AnimalSalesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks GetAnimalSalesByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID animalSalesId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final AnimalSales existingAnimalSales = AnimalSales.with(
            AnimalSalesID.from(animalSalesId), AnimalID.from(animalId), LocalDate.of(2024, 1, 1),
            Reason.VOLUNTARIA, BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450));

    @Test
    void shouldReturnAnimalSalesWhenItBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalSalesID.from(animalSalesId), AnimalID.from(animalId)))
                .thenReturn(Optional.of(existingAnimalSales));

        final var output = useCase.execute(new GetAnimalSalesByIdCommand(propertyId, animalId,
                animalSalesId, "tecnico@idr.com"));

        assertEquals(BigDecimal.valueOf(1500), output.value());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalSalesBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(AnimalSalesID.from(animalSalesId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetAnimalSalesByIdCommand(propertyId, animalId,
                        animalSalesId, "tecnico@idr.com")));
    }
}
