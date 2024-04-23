package br.edu.utfpr.ProjetoIDRAPI.Test.search.sort;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.utfpr.ProjetoIDRAPI.search.request.order.SearchSort;
import br.edu.utfpr.ProjetoIDRAPI.search.request.order.SearchSortAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;


public class SearchSortAdapterTest {

    @Test
    @DisplayName("Should provide a default sort method")
    public void defaultSortTest() {
        assertNotNull(SearchSortAdapter.defaultSort());
    }

    @Test
    @DisplayName("Should provide a sort by ASC type")
    public void byASCTypeTest() {
        Sort sort = SearchSortAdapter.byType(SearchSort.Type.ASC, "id");
        assertTrue(sort.isSorted());
        assertEquals(Sort.Order.asc("id"), sort.getOrderFor("id"));
    }

    @Test
    @DisplayName("Should provide a sort by DESC type")
    public void byDESCTypeTest() {
        Sort sort = SearchSortAdapter.byType(SearchSort.Type.DESC, "id");
        assertTrue(sort.isSorted());
        assertEquals(Sort.Order.desc("id"), sort.getOrderFor("id"));
    }

    @Test
    @DisplayName("Should provide default when adappted from null value")
    public void adaptTest() {
        Sort sort = SearchSortAdapter.adapt(null);
        assertNotNull(sort);
        assertFalse(sort.isSorted());
        assertEquals(Sort.unsorted(), sort);
    }

    @Test
    @DisplayName("Should provide the correct sort object when adapted from a SearchSort object")
    public void adaptFromSearchSortTest() {
        SearchSort searchSort = new SearchSort("id", SearchSort.Type.ASC);
        Sort sort = SearchSortAdapter.adapt(searchSort);
        assertTrue(sort.isSorted());
        assertEquals(Sort.Order.asc("id"), sort.getOrderFor("id"));
    }
}
