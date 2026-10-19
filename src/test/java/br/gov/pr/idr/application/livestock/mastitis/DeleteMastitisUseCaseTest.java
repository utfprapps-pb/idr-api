package br.gov.pr.idr.application.livestock.mastitis;

import br.gov.pr.idr.application.livestock.mastitis.delete.DeleteMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.delete.DeleteMastitisUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisID;
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
@DisplayName("DeleteMastitisUseCase")
class DeleteMastitisUseCaseTest {

    @Mock MastitisGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks DeleteMastitisUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID mastitisId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final Mastitis existingMastitis = Mastitis.with(MastitisID.from(mastitisId), AnimalID.from(animalId),
            LocalDate.now(), "Clinica", "+", "Positivo", "Negativo", "Positivo", "Negativo");

    private DeleteMastitisCommand validCommand() {
        return new DeleteMastitisCommand(propertyId, animalId, mastitisId, "tecnico@idr.com");
    }

    @Test
    void shouldDeleteWhenMastitisBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(MastitisID.from(mastitisId), AnimalID.from(animalId))).thenReturn(Optional.of(existingMastitis));

        useCase.execute(validCommand());

        verify(gateway).deleteById(MastitisID.from(mastitisId));
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));
        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).deleteById(any());
    }

    @Test
    void shouldThrowNotFoundWhenMastitisBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(MastitisID.from(mastitisId), AnimalID.from(animalId))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(validCommand()));
        verify(gateway, never()).deleteById(any());
    }
}
