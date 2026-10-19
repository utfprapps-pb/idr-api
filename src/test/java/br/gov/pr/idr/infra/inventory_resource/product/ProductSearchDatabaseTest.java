package br.gov.pr.idr.infra.inventory_resource.product;

import br.gov.pr.idr.infra.inventory_resource.product.persistence.ProductJPARepository;
import br.gov.pr.idr.infra.shared.persistence.LikeSpecification;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PersistenceAdapterTest
@DisplayName("Busca de produtos — Postgres real")
class ProductSearchDatabaseTest {

    @Autowired ProductJPARepository repository;

    private long count(final String terms) {
        return repository.search(LikeSpecification.containsPattern(terms), Pageable.unpaged()).getTotalElements();
    }

    @Test
    @DisplayName("busca sem diferenciar maiúsculas e trata % e _ como texto, não como curinga")
    void shouldMatchWildcardsLiterally() {
        assertEquals(1, count("PRODUCT 1"));
        assertEquals(0, count("%"));
        assertEquals(0, count("_"));
        assertEquals(count(null), count(""));
    }
}
