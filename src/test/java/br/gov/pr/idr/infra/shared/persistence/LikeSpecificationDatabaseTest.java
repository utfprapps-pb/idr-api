package br.gov.pr.idr.infra.shared.persistence;

import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPARepository;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedSpecification;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PersistenceAdapterTest
@DisplayName("LikeSpecification — Postgres real")
class LikeSpecificationDatabaseTest {

    @Autowired BreedJPARepository repository;

    private long count(final String terms) {
        return repository.count(BreedSpecification.withTerms(terms));
    }

    @Test
    @DisplayName("busca sem diferenciar maiúsculas e trata % e _ como texto, não como curinga")
    void shouldMatchWildcardsLiterally() {
        final long seeded = count(null);

        assertEquals(1, count("HOLANDÊS"));
        assertEquals(0, count("%"));
        assertEquals(0, count("_"));
        assertEquals(seeded, count(" ".repeat(2)));
    }
}
