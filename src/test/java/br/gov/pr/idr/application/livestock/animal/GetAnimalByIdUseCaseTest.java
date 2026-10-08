package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAnimalByIdUseCase")
class GetAnimalByIdUseCaseTest {

    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks GetAnimalByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final Animal existingAnimal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "Mimosa", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    @Test
    void shouldReturnAnimalWhenAuthorized() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(existingAnimal);

        final var output = useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, "tecnico@idr.com"));

        assertEquals("Mimosa", output.identifier());
    }

    @Test
    void shouldThrowNotFoundWhenAnimalBelongsToAnotherProperty() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com"))
                .thenThrow(NotFoundException.with("Animal com o ID %s não encontrado".formatted(animalId)));

        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetAnimalByIdCommand(propertyId, animalId, "tecnico@idr.com")));
    }
}
