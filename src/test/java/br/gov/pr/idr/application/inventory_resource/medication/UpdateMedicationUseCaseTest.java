package br.gov.pr.idr.application.inventory_resource.medication;

import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationID;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateMedicationUseCase")
class UpdateMedicationUseCaseTest {

    @Mock MedicationGateway gateway;
    @Mock ProductGateway productGateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks UpdateMedicationUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID medicationId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final Medication existingMedication = Medication.with(MedicationID.from(medicationId),
            AnimalID.from(animalId), ProductID.unique(), LocalDate.now(), "10ml", "IM");

    private UpdateMedicationCommand validCommand() {
        return new UpdateMedicationCommand(propertyId, animalId, medicationId, "tecnico@idr.com", productId,
                LocalDate.now(), "20ml", "SC");
    }

    @Test
    void shouldUpdateWhenMedicationBelongsToAnimalAndProductExists() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(MedicationID.from(medicationId), AnimalID.from(animalId))).thenReturn(Optional.of(existingMedication));
        when(productGateway.existsById(any())).thenReturn(true);
        when(gateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(validCommand());

        assertEquals("20ml", output.appliedDose());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(MedicationID.from(medicationId), AnimalID.from(animalId))).thenReturn(Optional.of(existingMedication));
        when(productGateway.existsById(any())).thenReturn(false);

        assertThrows(NotificationException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }

    @Test
    void shouldThrowNotFoundWhenMedicationBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(MedicationID.from(medicationId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).update(any());
    }
}
