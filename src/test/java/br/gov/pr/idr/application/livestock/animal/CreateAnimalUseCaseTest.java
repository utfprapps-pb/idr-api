package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAnimalUseCase")
class CreateAnimalUseCaseTest {

    @Mock AnimalGateway animalGateway;
    @Mock BreedGateway breedGateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks CreateAnimalUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID breedId = UUID.randomUUID();
    private final Property property = Property.with(PropertyID.from(propertyId), "Fazenda", null, null, null,
            null, null, null, null, ProducerID.unique(), CityID.unique(), List.of(), List.of(), null, null);

    private CreateAnimalCommand validCommand() {
        return new CreateAnimalCommand(propertyId, "tecnico@idr.com", null, breedId, "Leiteiro", "Mimosa", "F",
                "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    }

    @Test
    void shouldCreateWhenAuthorizedAndBreedExists() {
        when(accessPolicy.ensureAccessToProperty(propertyId, "tecnico@idr.com")).thenReturn(property);
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
        when(accessPolicy.ensureAccessToProperty(any(), any())).thenThrow(ForbiddenException.with("x"));
        assertThrows(ForbiddenException.class, () -> useCase.execute(validCommand()));
        verify(animalGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenBreedDoesNotExist() {
        when(accessPolicy.ensureAccessToProperty(any(), any())).thenReturn(property);
        when(breedGateway.existsById(any())).thenReturn(false);
        assertThrows(NotificationException.class, () -> useCase.execute(validCommand()));
    }

    @Test
    void shouldThrowWhenAnimalMotherIdDoesNotExist() {
        when(accessPolicy.ensureAccessToProperty(any(), any())).thenReturn(property);
        when(breedGateway.existsById(any())).thenReturn(true);
        when(animalGateway.findById(any())).thenReturn(Optional.empty());
        final var command = new CreateAnimalCommand(propertyId, "tecnico@idr.com", UUID.randomUUID(), breedId,
                "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenAnimalMotherBelongsToDifferentProperty() {
        when(accessPolicy.ensureAccessToProperty(any(), any())).thenReturn(property);
        when(breedGateway.existsById(any())).thenReturn(true);

        final var motherId = UUID.randomUUID();
        final var motherFromAnotherProperty = Animal.with(AnimalID.from(motherId),
                PropertyID.from(UUID.randomUUID()), null, BreedID.unique(), "T", "Mae", "F", "Vivo",
                LocalDate.now(), 1f, 1f, 1f, 1f);
        when(animalGateway.findById(AnimalID.from(motherId))).thenReturn(Optional.of(motherFromAnotherProperty));

        final var command = new CreateAnimalCommand(propertyId, "tecnico@idr.com", motherId, breedId,
                "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
    }
}
