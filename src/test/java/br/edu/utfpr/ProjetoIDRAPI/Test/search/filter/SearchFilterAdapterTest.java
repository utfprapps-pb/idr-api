package br.edu.utfpr.ProjetoIDRAPI.Test.search.filter;

import br.edu.utfpr.ProjetoIDRAPI.search.request.filter.SearchFilter;
import br.edu.utfpr.ProjetoIDRAPI.search.request.filter.SearchFilterAdapter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SearchFilterAdapterTest {

    SearchFilterAdapter adapter = new SearchFilterAdapter();

    SearchFilter filter = new SearchFilter("id", 1, SearchFilter.Type.EQUALS);

    @Mock
    Root<Object> root;

    @Mock
    CriteriaQuery<Object> query;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(criteriaBuilder.equal(root.get("id"), 1)).thenReturn(Mockito.mock(Predicate.class));
    }

    @Test
    @DisplayName("Test adapt method")
    void testAdapt() {
        Predicate predicate = adapter.adapt(filter, root, query, criteriaBuilder);
        assertNotNull(predicate);
    }

    @Test
    @DisplayName("Test toPredicate method: EQUALS")
    void testToPredicateEquals() {
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).equal(root.get("id"), 1);
    }


    @Test
    @DisplayName("Test toPredicate method: NOT_EQUALS")
    void testToPredicateNotEquals() {
        filter.setType(SearchFilter.Type.NOT_EQUALS);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).notEqual(root.get("id"), 1);
    }

    @Test
    @DisplayName("Test toPredicate method: LIKE")
    void testToPredicateLike() {
        filter.setType(SearchFilter.Type.LIKE);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).like(root.get("id"), "%" + 1 + "%");
    }

    @Test
    @DisplayName("Test toPredicate method: NOT_LIKE")
    void testToPredicateNotLike() {
        filter.setType(SearchFilter.Type.NOT_LIKE);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).notLike(root.get("id"), "%" + 1 + "%");
    }

    @Test
    @DisplayName("Test toPredicate method: GREATER")
    void testToPredicateGreater() {
        filter.setType(SearchFilter.Type.GREATER);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).greaterThan(root.get("id"), "1");
    }

    @Test
    @DisplayName("Test toPredicate method: LESS")
    void testToPredicateLess() {
        filter.setType(SearchFilter.Type.LESS);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).lessThan(root.get("id"), "1");
    }

    @Test
    @DisplayName("Test toPredicate method: GREATER_EQUALS")
    void testToPredicateGreaterEquals() {
        filter.setType(SearchFilter.Type.GREATER_EQUALS);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).greaterThanOrEqualTo(root.get("id"), "1");
    }

    @Test
    @DisplayName("Test toPredicate method: LESS_EQUALS")
    void testToPredicateLessEquals() {
        filter.setType(SearchFilter.Type.LESS_EQUALS);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).lessThanOrEqualTo(any(), anyString());
    }

    @Test
    @DisplayName("Test toPredicate method: IS_NULL")
    void testToPredicateIsNull() {
        filter.setType(SearchFilter.Type.IS_NULL);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).isNull(any());
    }

    @Test
    @DisplayName("Test toPredicate method: IS_NOT_NULL")
    void testToPredicateIsNotNull() {
        filter.setType(SearchFilter.Type.IS_NOT_NULL);
        adapter.adapt(filter, root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).isNotNull(any());
    }

}
