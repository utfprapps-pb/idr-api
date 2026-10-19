package br.gov.pr.idr.application.livestock.insemination;

import br.gov.pr.idr.application.livestock.insemination.delete.DeleteInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.delete.DeleteInseminationUseCase;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteInseminationUseCase")
class DeleteInseminationUseCaseTest {

    @Mock InseminationGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks DeleteInseminationUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID inseminationId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final Insemination existingInsemination = Insemination.with(InseminationID.from(inseminationId),
            AnimalID.from(animalId), LocalDate.now(), "Touro 1");

    private DeleteInseminationCommand validCommand() {
        return new DeleteInseminationCommand(propertyId, animalId, inseminationId, "tecnico@idr.com");
    }

    @Test
    void shouldDeleteWhenInseminationBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(InseminationID.from(inseminationId), AnimalID.from(animalId))).thenReturn(Optional.of(existingInsemination));

        useCase.execute(validCommand());

        verify(gateway).deleteById(InseminationID.from(inseminationId));
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).deleteById(any());
    }

    @Test
    void shouldThrowNotFoundWhenInseminationBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(InseminationID.from(inseminationId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).deleteById(any());
    }
}
