package br.gov.pr.idr.infra.livestock.shared;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.infra.livestock.animal.AnimalPostgresGateway;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository;
import br.gov.pr.idr.infra.livestock.breed.BreedPostgresGateway;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPARepository;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PersistenceAdapterTest
@DisplayName("PostgresLivestockNameLookup — Postgres real")
class PostgresLivestockNameLookupDatabaseTest {

    @Autowired BreedJPARepository breedRepository;
    @Autowired AnimalJPARepository animalRepository;
    @Autowired TestEntityManager entityManager;
    PostgresLivestockNameLookup lookup;
    BreedPostgresGateway breeds;
    AnimalPostgresGateway animals;

    @BeforeEach
    void setUp() {
        lookup = new PostgresLivestockNameLookup(breedRepository, animalRepository);
        breeds = new BreedPostgresGateway(breedRepository);
        animals = new AnimalPostgresGateway(animalRepository);
    }

    @Test
    @DisplayName("breedNames: projeção devolve id → nome e omite raça excluída (soft delete) e id desconhecido")
    void shouldResolveBreedNamesAndSkipDeletedOnes() {
        final var gir = breeds.save(Breed.create("Gir Teste"));
        final var deleted = breeds.save(Breed.create("Raça Excluída"));
        entityManager.flush();
        breeds.deleteById(deleted.getId());
        entityManager.flush();
        entityManager.clear();

        final var names = lookup.breedNames(List.of(gir.getId(), deleted.getId(), BreedID.unique()));

        assertEquals(Map.of(gir.getId(), "Gir Teste"), names);
    }

    @Test
    @DisplayName("animalIdentifiers: projeção devolve id → identificador")
    void shouldResolveAnimalIdentifiers() {
        final var animal = animals.save(Animal.with(AnimalID.unique(), PropertyID.unique(), null, BreedID.unique(),
                "T", "Bezerra Teste", "F", "Vivo", LocalDate.of(2024, 1, 1), 1f, 1f, 1f, 1f));
        entityManager.flush();
        entityManager.clear();

        assertEquals("Bezerra Teste", lookup.animalIdentifier(animal.getId()));
    }
}
