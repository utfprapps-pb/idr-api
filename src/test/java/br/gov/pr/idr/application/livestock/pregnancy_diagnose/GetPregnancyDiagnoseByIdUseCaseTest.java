package br.gov.pr.idr.application.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseID;
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
@DisplayName("GetPregnancyDiagnoseByIdUseCase")
class GetPregnancyDiagnoseByIdUseCaseTest {

    @Mock PregnancyDiagnoseGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks GetPregnancyDiagnoseByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID pregnancyDiagnoseId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final PregnancyDiagnose existingPregnancyDiagnose = PregnancyDiagnose.with(
            PregnancyDiagnoseID.from(pregnancyDiagnoseId), AnimalID.from(animalId), LocalDate.now(),
            LocalDate.of(2023, 11, 1));

    @Test
    void shouldReturnPregnancyDiagnoseWhenItBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(PregnancyDiagnoseID.from(pregnancyDiagnoseId), AnimalID.from(animalId)))
                .thenReturn(Optional.of(existingPregnancyDiagnose));

        final var output = useCase.execute(new GetPregnancyDiagnoseByIdCommand(propertyId, animalId,
                pregnancyDiagnoseId, PropertySearchScope.unrestricted()));

        assertEquals(LocalDate.of(2023, 11, 1), output.lastIa());
    }

    @Test
    void shouldThrowNotFoundWhenPregnancyDiagnoseBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(PregnancyDiagnoseID.from(pregnancyDiagnoseId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetPregnancyDiagnoseByIdCommand(propertyId, animalId,
                        pregnancyDiagnoseId, PropertySearchScope.unrestricted())));
    }
}
