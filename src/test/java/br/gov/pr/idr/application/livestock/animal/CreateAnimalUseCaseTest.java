package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAnimalUseCase")
class CreateAnimalUseCaseTest {

    @Mock AnimalGateway animalGateway;
    @Mock BreedGateway breedGateway;
    @Mock AnimalAccessPolicy accessPolicy;
    CreateAnimalUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateAnimalUseCase(animalGateway, new LivestockReferencePolicy(breedGateway, animalGateway),
                accessPolicy);
    }

    private final UUID propertyId = UUID.randomUUID();
    private final UUID breedId = UUID.randomUUID();

    private CreateAnimalCommand validCommand() {
        return new CreateAnimalCommand(propertyId, "tecnico@idr.com", null, breedId, "Leiteiro", "Mimosa", "F",
                "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    }

    @Test
    void shouldCreateWhenAuthorizedAndBreedExists() {
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        final var command = new CreateAnimalCommand(propertyId, "tecnico@idr.com", null, breedId, "Leiteiro",
                "Mimosa", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

        final var output = useCase.execute(command);

        assertEquals("Mimosa", output.identifier());
        verify(animalGateway).save(any());
    }

    @Test
    void shouldThrowForbiddenWhenAccessGuardDenies() {
        doThrow(ForbiddenException.with("x")).when(accessPolicy).ensureAccessToProperty(any(), any());
        assertThrows(ForbiddenException.class, () -> useCase.execute(validCommand()));
        verify(animalGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenBreedDoesNotExist() {
        when(breedGateway.existsById(any())).thenReturn(false);
        assertThrows(NotificationException.class, () -> useCase.execute(validCommand()));
    }

    @Test
    void shouldThrowWhenAnimalMotherIdDoesNotExist() {
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.existsByIdAndPropertyId(any(), any())).thenReturn(false);
        final var command = new CreateAnimalCommand(propertyId, "tecnico@idr.com", UUID.randomUUID(), breedId,
                "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenAnimalMotherBelongsToDifferentProperty() {
        when(breedGateway.existsById(any())).thenReturn(true);

        final var motherId = UUID.randomUUID();
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(motherId), PropertyID.from(propertyId)))
                .thenReturn(false);

        final var command = new CreateAnimalCommand(propertyId, "tecnico@idr.com", motherId, breedId,
                "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }
}
