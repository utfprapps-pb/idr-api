package br.gov.pr.idr.application.livestock.animal_sales;

import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesGateway;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAnimalSalesUseCase")
class CreateAnimalSalesUseCaseTest {

    @Mock AnimalSalesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks CreateAnimalSalesUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    private CreateAnimalSalesCommand validCreateCommand() {
        return new CreateAnimalSalesCommand(propertyId, animalId, "tecnico@idr.com",
                LocalDate.of(2024, 1, 1), Reason.VOLUNTARIA, BigDecimal.valueOf(1500), Destination.ABATE);
    }

    @Test
    void shouldCreateWhenAnimalIsAccessible() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(validCreateCommand());
        assertEquals(BigDecimal.valueOf(1500), output.value());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCreateCommand()));
        verify(gateway, never()).save(any());
    }
}
