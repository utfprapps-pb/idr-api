package br.gov.pr.idr.application.livestock.animal_childbirth;

import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateAnimalChildbirthUseCase")
class UpdateAnimalChildbirthUseCaseTest {

    @Mock AnimalChildbirthGateway gateway;
    @Mock BreedGateway breedGateway;
    @Mock AnimalGateway animalGateway;
    @Mock AnimalAccessPolicy accessPolicy;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID breedId = UUID.randomUUID();
    private final UUID childbirthId = UUID.randomUUID();
    private final Animal mother = animal(animalId, propertyId, "Mimosa");

    private static Animal animal(final UUID id, final UUID propertyId, final String identifier) {
        return Animal.with(AnimalID.from(id), PropertyID.from(propertyId), null, BreedID.unique(), "T",
                identifier, "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    }

    private AnimalChildbirth childbirthOf(final UUID ownerId, final UUID calfId) {
        return AnimalChildbirth.with(AnimalChildbirthID.from(childbirthId), AnimalID.from(ownerId),
                LocalDate.of(2024, 3, 10), ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE,
                BreedID.from(breedId), calfId == null ? null : AnimalID.from(calfId));
    }
    UpdateAnimalChildbirthUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateAnimalChildbirthUseCase(gateway, new ChildbirthReferencePolicy(breedGateway, animalGateway), accessPolicy);
    }

    private UpdateAnimalChildbirthCommand command(final UUID calfId) {
        return new UpdateAnimalChildbirthCommand(propertyId, animalId, childbirthId, "tecnico@idr.com",
                LocalDate.of(2024, 4, 1), ChildbirthGender.MALE, new BigDecimal("40"), ChildbirthCondition.DEAD,
                breedId, calfId);
    }

    @Test
    void shouldUpdateWhenChildbirthBelongsToAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(mother);
        when(gateway.findById(AnimalChildbirthID.from(childbirthId)))
                .thenReturn(Optional.of(childbirthOf(animalId, null)));
        when(breedGateway.existsById(BreedID.from(breedId))).thenReturn(true);
        when(gateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(command(null));

        assertEquals(ChildbirthGender.MALE, output.gender());
        assertEquals(ChildbirthCondition.DEAD, output.condition());
        assertEquals(new BigDecimal("40"), output.weight());
    }

    @Test
    void shouldThrowNotFoundWhenChildbirthBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(mother);
        when(gateway.findById(AnimalChildbirthID.from(childbirthId)))
                .thenReturn(Optional.of(childbirthOf(UUID.randomUUID(), null)));

        assertThrows(NotFoundException.class, () -> useCase.execute(command(null)));
        verify(gateway, never()).update(any());
    }

    @Test
    void shouldRejectCalfFromAnotherPropertyOnUpdate() {
        final var calfId = UUID.randomUUID();
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(mother);
        when(gateway.findById(AnimalChildbirthID.from(childbirthId)))
                .thenReturn(Optional.of(childbirthOf(animalId, null)));
        when(breedGateway.existsById(BreedID.from(breedId))).thenReturn(true);
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(calfId), PropertyID.from(propertyId)))
                .thenReturn(false);

        assertThrows(NotificationException.class, () -> useCase.execute(command(calfId)));
        verify(gateway, never()).update(any());
    }
}
