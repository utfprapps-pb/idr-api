package br.gov.pr.idr.domain.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AnimalChildbirth — Aggregate")
class AnimalChildbirthTest {

    private static AnimalChildbirth create(final AnimalID motherId, final BreedID breedId, final AnimalID calfId) {
        return AnimalChildbirth.create(motherId, LocalDate.of(2024, 1, 1), ChildbirthGender.FEMALE,
                new BigDecimal("35.5"), ChildbirthCondition.ALIVE, breedId, calfId);
    }

    @Test
    void shouldCreateWithoutCalf() {
        final var childbirth = create(AnimalID.unique(), BreedID.unique(), null);
        assertNotNull(childbirth.getId());
        assertEquals(Optional.empty(), childbirth.getCalfId());
    }

    @Test
    void shouldCreateWithCalf() {
        final var calfId = AnimalID.unique();
        assertEquals(Optional.of(calfId), create(AnimalID.unique(), BreedID.unique(), calfId).getCalfId());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> create(null, BreedID.unique(), null));
    }

    @Test
    void shouldFailWhenBreedIsNull() {
        assertThrows(NotificationException.class, () -> create(AnimalID.unique(), null, null));
    }

    @Test
    @DisplayName("o bezerro não pode ser a própria mãe")
    void shouldFailWhenCalfIsTheMother() {
        final var motherId = AnimalID.unique();
        assertThrows(NotificationException.class, () -> create(motherId, BreedID.unique(), motherId));
    }
}
