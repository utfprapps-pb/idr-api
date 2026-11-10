package br.gov.pr.idr.infra.livestock.animal;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PersistenceAdapterTest
@DisplayName("AnimalPostgresGateway — Postgres real")
class AnimalPostgresGatewayDatabaseTest {

    @Autowired AnimalJPARepository repository;
    @Autowired TestEntityManager entityManager;
    AnimalPostgresGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new AnimalPostgresGateway(repository);
    }

    private Animal savedAnimalIn(final PropertyID propertyId) {
        final var animal = gateway.save(Animal.with(AnimalID.unique(), propertyId, null, BreedID.unique(), "T",
                "Teste", "F", "Vivo", LocalDate.of(2020, 1, 1), 1f, 1f, 1f, 1f));
        entityManager.flush();
        return animal;
    }

    @Test
    @DisplayName("existsByIdAndPropertyId: só é verdadeiro para o animal na sua própria propriedade")
    void shouldAnswerOwnershipFromTheDatabase() {
        final var propertyId = PropertyID.unique();
        final var animal = savedAnimalIn(propertyId);

        assertTrue(gateway.existsByIdAndPropertyId(animal.getId(), propertyId));
        assertFalse(gateway.existsByIdAndPropertyId(animal.getId(), PropertyID.unique()));
        assertFalse(gateway.existsByIdAndPropertyId(AnimalID.unique(), propertyId));
    }

    @Test
    @DisplayName("existsByIdAndPropertyId: animal excluído (soft delete) não conta")
    void shouldIgnoreSoftDeletedAnimals() {
        final var propertyId = PropertyID.unique();
        final var animal = savedAnimalIn(propertyId);

        gateway.deleteById(animal.getId());
        entityManager.flush();
        entityManager.clear();

        assertFalse(gateway.existsByIdAndPropertyId(animal.getId(), propertyId));
    }
}
