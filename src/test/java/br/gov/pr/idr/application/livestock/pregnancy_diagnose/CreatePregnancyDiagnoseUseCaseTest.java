package br.gov.pr.idr.application.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePregnancyDiagnoseUseCase")
class CreatePregnancyDiagnoseUseCaseTest {

    @Mock PregnancyDiagnoseGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks CreatePregnancyDiagnoseUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    private CreatePregnancyDiagnoseCommand validCreateCommand() {
        return new CreatePregnancyDiagnoseCommand(propertyId, animalId, PropertySearchScope.unrestricted(), LocalDate.now(),
                LocalDate.of(2023, 11, 1));
    }

    @Test
    void shouldCreateWhenAnimalIsAccessible() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(validCreateCommand());
        assertEquals(LocalDate.of(2023, 11, 1), output.lastIa());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCreateCommand()));
        verify(gateway, never()).save(any());
    }
}
