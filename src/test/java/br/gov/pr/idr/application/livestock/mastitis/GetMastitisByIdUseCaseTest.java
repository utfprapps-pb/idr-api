package br.gov.pr.idr.application.livestock.mastitis;

import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdCommand;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdUseCase;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetMastitisByIdUseCase")
class GetMastitisByIdUseCaseTest {

    @Mock MastitisGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks GetMastitisByIdUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID mastitisId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final Mastitis existingMastitis = Mastitis.with(MastitisID.from(mastitisId), AnimalID.from(animalId),
            LocalDate.now(), "Clinica", "+", "Positivo", "Negativo", "Positivo", "Negativo");

    @Test
    void shouldReturnMastitisWhenItBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(MastitisID.from(mastitisId), AnimalID.from(animalId))).thenReturn(Optional.of(existingMastitis));

        final var output = useCase.execute(new GetMastitisByIdCommand(propertyId, animalId, mastitisId,
                "tecnico@idr.com"));

        assertEquals("Clinica", output.mastitisType());
    }

    @Test
    void shouldThrowNotFoundWhenMastitisBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.findByIdAndAnimalId(MastitisID.from(mastitisId), AnimalID.from(animalId))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new GetMastitisByIdCommand(propertyId, animalId, mastitisId,
                        "tecnico@idr.com")));
    }
}
