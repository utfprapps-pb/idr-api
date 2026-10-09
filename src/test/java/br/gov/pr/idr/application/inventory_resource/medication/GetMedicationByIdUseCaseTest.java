package br.gov.pr.idr.application.inventory_resource.medication;

import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdCommand;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationID;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetMedicationByIdUseCase")
class GetMedicationByIdUseCaseTest {

    @Mock MedicationGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks GetMedicationByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID medicationId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final Medication existingMedication = Medication.with(MedicationID.from(medicationId),
            AnimalID.from(animalId), ProductID.unique(), LocalDate.now(), "10ml", "IM");
    private final Medication medicationOfAnotherAnimal = Medication.with(MedicationID.from(medicationId),
            AnimalID.unique(), ProductID.unique(), LocalDate.now(), "10ml", "IM");

    @Test
    void shouldReturnMedicationWhenItBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findById(MedicationID.from(medicationId))).thenReturn(Optional.of(existingMedication));

        final var output = useCase.execute(new GetMedicationByIdCommand(propertyId, animalId, medicationId,
                "tecnico@idr.com"));

        assertEquals("10ml", output.appliedDose());
    }

    @Test
    void shouldThrowNotFoundWhenMedicationBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findById(MedicationID.from(medicationId)))
                .thenReturn(Optional.of(medicationOfAnotherAnimal));
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetMedicationByIdCommand(propertyId, animalId, medicationId,
                        "tecnico@idr.com")));
    }
}
