package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.application.livestock.animal.retrieve.find.FindAnimalByIdentifierCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.find.FindAnimalByIdentifierUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindAnimalByIdentifierUseCase")
class FindAnimalByIdentifierUseCaseTest {

    @Mock AnimalGateway animalGateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks FindAnimalByIdentifierUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final Property property = Property.with(PropertyID.from(propertyId), "Fazenda", null, null, null,
            null, null, null, null, ProducerID.unique(), CityID.unique(), List.of(), List.of(), null, null);

    @Test
    void shouldReturnEmptyWhenIdentifierNotFoundWithinAuthorizedProperty() {
        when(accessPolicy.ensureAccessToProperty(propertyId, "tecnico@idr.com")).thenReturn(property);
        when(animalGateway.findByPropertyIdAndIdentifier(PropertyID.from(propertyId), "Inexistente"))
                .thenReturn(Optional.empty());

        final var output = useCase.execute(
                new FindAnimalByIdentifierCommand(propertyId, "Inexistente", "tecnico@idr.com"));

        assertTrue(output.isEmpty());
    }

    @Test
    void shouldReturnAnimalWhenFoundWithinAuthorizedProperty() {
        when(accessPolicy.ensureAccessToProperty(propertyId, "tecnico@idr.com")).thenReturn(property);
        final var animal = Animal.with(AnimalID.unique(), PropertyID.from(propertyId), null, BreedID.unique(),
                "T", "Mimosa", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        when(animalGateway.findByPropertyIdAndIdentifier(PropertyID.from(propertyId), "Mimosa"))
                .thenReturn(Optional.of(animal));

        final var output = useCase.execute(
                new FindAnimalByIdentifierCommand(propertyId, "Mimosa", "tecnico@idr.com"));

        assertTrue(output.isPresent());
        assertEquals("Mimosa", output.get().identifier());
    }

    @Test
    void shouldThrowForbiddenWhenAccessGuardDenies() {
        when(accessPolicy.ensureAccessToProperty(propertyId, "tecnico@idr.com"))
                .thenThrow(ForbiddenException.with("x"));

        assertThrows(ForbiddenException.class, () -> useCase.execute(
                new FindAnimalByIdentifierCommand(propertyId, "Mimosa", "tecnico@idr.com")));
    }
}
