package br.gov.pr.idr.application.inventory_resource.medication;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
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
@DisplayName("CreateMedicationUseCase")
class CreateMedicationUseCaseTest {

    @Mock MedicationGateway gateway;
    @Mock ProductGateway productGateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks CreateMedicationUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    private CreateMedicationCommand validCreateCommand() {
        return new CreateMedicationCommand(propertyId, animalId, PropertySearchScope.unrestricted(), productId, LocalDate.now(),
                "10ml", "IM");
    }

    @Test
    void shouldCreateWhenAnimalIsAccessibleAndProductExists() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(productGateway.existsById(any())).thenReturn(true);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new CreateMedicationCommand(propertyId, animalId, PropertySearchScope.unrestricted(),
                productId, LocalDate.now(), "10ml", "IM"));

        assertEquals("10ml", output.appliedDose());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(animal);
        when(productGateway.existsById(any())).thenReturn(false);
        assertThrows(NotificationException.class, () -> useCase.execute(validCreateCommand()));
        verify(gateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(ForbiddenException.with("x"));
        assertThrows(ForbiddenException.class, () -> useCase.execute(validCreateCommand()));
    }
}
