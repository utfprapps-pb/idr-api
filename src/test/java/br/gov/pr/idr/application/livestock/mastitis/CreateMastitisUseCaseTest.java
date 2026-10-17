package br.gov.pr.idr.application.livestock.mastitis;

import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;
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
@DisplayName("CreateMastitisUseCase")
class CreateMastitisUseCaseTest {

    @Mock MastitisGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks CreateMastitisUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    private CreateMastitisCommand validCreateCommand() {
        return new CreateMastitisCommand(propertyId, animalId, "tecnico@idr.com", LocalDate.now(), "Clinica", "+",
                "Positivo", "Negativo", "Positivo", "Negativo");
    }

    @Test
    void shouldCreateWhenAnimalIsAccessible() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(validCreateCommand());
        assertEquals("Clinica", output.mastitisType());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCreateCommand()));
        verify(gateway, never()).save(any());
    }
}
