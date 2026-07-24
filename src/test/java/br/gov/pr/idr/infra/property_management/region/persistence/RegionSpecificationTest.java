package br.gov.pr.idr.infra.property_management.region.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegionSpecification")
class RegionSpecificationTest {

    @Mock Root<RegionJPAEntity> root;
    @Mock CriteriaQuery<?> query;
    @Mock CriteriaBuilder cb;
    @Mock Predicate conjunctionPredicate;

    @Test
    @DisplayName("deve retornar conjunction quando terms é nulo")
    void shouldReturnConjunctionWhenTermsIsNull() {
        when(cb.conjunction()).thenReturn(conjunctionPredicate);

        final var predicate = RegionSpecification.withTerms(null).toPredicate(root, query, cb);

        assertEquals(conjunctionPredicate, predicate);
        verifyNoInteractions(root);
    }

    @Test
    @DisplayName("deve retornar conjunction quando terms está em branco")
    void shouldReturnConjunctionWhenTermsIsBlank() {
        when(cb.conjunction()).thenReturn(conjunctionPredicate);

        final var predicate = RegionSpecification.withTerms("   ").toPredicate(root, query, cb);

        assertEquals(conjunctionPredicate, predicate);
        verifyNoInteractions(root);
    }

    @Test
    @DisplayName("deve retornar predicate like quando terms é informado")
    void shouldReturnLikePredicateWhenTermsIsPresent() {
        final Path<String> path = mock(Path.class);
        final Expression<String> lowerExpr = mock(Expression.class);
        final Predicate likePredicate = mock(Predicate.class);

        when(root.<String>get("description")).thenReturn(path);
        when(cb.lower(path)).thenReturn(lowerExpr);
        when(cb.like(lowerExpr, "%curitiba%")).thenReturn(likePredicate);

        final var predicate = RegionSpecification.withTerms("Curitiba").toPredicate(root, query, cb);

        assertEquals(likePredicate, predicate);
    }
}
