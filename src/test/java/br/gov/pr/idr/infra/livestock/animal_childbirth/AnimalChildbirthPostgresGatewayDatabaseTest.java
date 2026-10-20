package br.gov.pr.idr.infra.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_childbirth.persistence.AnimalChildbirthJPARepository;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PersistenceAdapterTest
@DisplayName("AnimalChildbirthPostgresGateway — Postgres real")
class AnimalChildbirthPostgresGatewayDatabaseTest {

    @Autowired AnimalChildbirthJPARepository repository;
    @Autowired TestEntityManager entityManager;
    AnimalChildbirthPostgresGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new AnimalChildbirthPostgresGateway(repository);
    }

    @Test
    @DisplayName("grava e relê enums, peso, raça e bezerro do banco")
    void shouldRoundtripEveryFieldThroughTheDatabase() {
        final var calfId = AnimalID.unique();
        final var breedId = BreedID.unique();
        final var saved = gateway.save(AnimalChildbirth.create(AnimalID.unique(), LocalDate.of(2024, 6, 10),
                ChildbirthGender.MALE, new BigDecimal("38.40"), ChildbirthCondition.DEAD, breedId, calfId));
        entityManager.flush();
        entityManager.clear();

        final var reloaded = gateway.findByIdAndAnimalId(saved.getId(), saved.getAnimalId()).orElseThrow();

        assertEquals(LocalDate.of(2024, 6, 10), reloaded.getChildbirthDate());
        assertEquals(ChildbirthGender.MALE, reloaded.getGender());
        assertEquals(ChildbirthCondition.DEAD, reloaded.getCondition());
        assertEquals(0, new BigDecimal("38.40").compareTo(reloaded.getWeight()));
        assertEquals(breedId, reloaded.getBreedId());
        assertEquals(Optional.of(calfId), reloaded.getCalfId());
    }

    @Test
    @DisplayName("só encontra o parto pela mãe dona dele, e não depois do soft delete")
    void shouldFindOnlyByOwnerAndNotAfterDelete() {
        final var saved = gateway.save(AnimalChildbirth.create(AnimalID.unique(), LocalDate.of(2024, 6, 10),
                ChildbirthGender.FEMALE, new BigDecimal("35"), ChildbirthCondition.ALIVE, BreedID.unique(), null));
        entityManager.flush();
        entityManager.clear();

        assertTrue(gateway.findByIdAndAnimalId(saved.getId(), saved.getAnimalId()).isPresent());
        assertTrue(gateway.findByIdAndAnimalId(saved.getId(), AnimalID.unique()).isEmpty());

        gateway.deleteById(saved.getId());
        entityManager.flush();
        entityManager.clear();

        assertTrue(gateway.findByIdAndAnimalId(saved.getId(), saved.getAnimalId()).isEmpty());
    }

    @Test
    @DisplayName("search devolve só os partos da mãe informada")
    void shouldScopeSearchByMother() {
        final var motherId = AnimalID.unique();
        gateway.save(AnimalChildbirth.create(motherId, LocalDate.of(2024, 6, 10), ChildbirthGender.FEMALE,
                new BigDecimal("35"), ChildbirthCondition.ALIVE, BreedID.unique(), null));
        gateway.save(AnimalChildbirth.create(AnimalID.unique(), LocalDate.of(2024, 6, 11), ChildbirthGender.MALE,
                new BigDecimal("36"), ChildbirthCondition.ALIVE, BreedID.unique(), null));
        entityManager.flush();

        final var page = gateway.search(motherId, SearchQuery.from(0, 10, "", "childbirthDate", "asc"));

        assertEquals(1, page.total());
        assertEquals(motherId, page.items().get(0).getAnimalId());
    }
}
