package br.gov.pr.idr.infra.livestock.animal_death;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_death.persistence.AnimalDeathJPARepository;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PersistenceAdapterTest
@DisplayName("AnimalDeathPostgresGateway — Postgres real")
class AnimalDeathPostgresGatewayDatabaseTest {

    @Autowired AnimalDeathJPARepository repository;
    @Autowired TestEntityManager entityManager;
    AnimalDeathPostgresGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new AnimalDeathPostgresGateway(repository);
    }

    @Test
    @DisplayName("existsByAnimalId acompanha o registro e ignora a morte excluída (soft delete)")
    void shouldTrackTheOneDeathOfAnAnimal() {
        final var animalId = AnimalID.unique();
        assertFalse(gateway.existsByAnimalId(animalId));

        final var death = gateway.save(AnimalDeath.create(animalId, "Pneumonia", LocalDate.of(2024, 5, 2)));
        entityManager.flush();
        assertTrue(gateway.existsByAnimalId(animalId));

        gateway.deleteById(death.getId());
        entityManager.flush();
        entityManager.clear();
        assertFalse(gateway.existsByAnimalId(animalId));
    }

    @Test
    @DisplayName("search devolve só as mortes do animal informado")
    void shouldScopeSearchByAnimal() {
        final var animalId = AnimalID.unique();
        gateway.save(AnimalDeath.create(animalId, "Pneumonia", LocalDate.of(2024, 5, 2)));
        gateway.save(AnimalDeath.create(AnimalID.unique(), "Outro animal", LocalDate.of(2024, 5, 3)));
        entityManager.flush();

        final var page = gateway.search(animalId, SearchQuery.from(0, 10, "", "deathDate", "asc"));

        assertEquals(1, page.total());
        assertEquals("Pneumonia", page.items().get(0).getReason());
    }
}
