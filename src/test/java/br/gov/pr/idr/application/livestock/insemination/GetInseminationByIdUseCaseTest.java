package br.gov.pr.idr.application.livestock.insemination;

import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdCommand;
import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationGateway;
import br.gov.pr.idr.domain.livestock.insemination.InseminationID;
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
@DisplayName("GetInseminationByIdUseCase")
class GetInseminationByIdUseCaseTest {

    @Mock InseminationGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks GetInseminationByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID inseminationId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final Insemination existingInsemination = Insemination.with(InseminationID.from(inseminationId),
            AnimalID.from(animalId), LocalDate.now(), "Touro 1");

    @Test
    void shouldReturnInseminationWhenItBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(InseminationID.from(inseminationId), AnimalID.from(animalId))).thenReturn(Optional.of(existingInsemination));

        final var output = useCase.execute(new GetInseminationByIdCommand(propertyId, animalId, inseminationId,
                "tecnico@idr.com"));

        assertEquals("Touro 1", output.bull());
    }

    @Test
    void shouldThrowNotFoundWhenInseminationBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(InseminationID.from(inseminationId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetInseminationByIdCommand(propertyId, animalId, inseminationId,
                        "tecnico@idr.com")));
    }
}
