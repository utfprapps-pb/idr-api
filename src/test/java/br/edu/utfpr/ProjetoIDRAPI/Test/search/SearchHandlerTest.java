package br.edu.utfpr.ProjetoIDRAPI.Test.search;

import br.edu.utfpr.ProjetoIDRAPI.search.SearchHandler;
import br.edu.utfpr.ProjetoIDRAPI.search.request.SearchRequest;
import br.edu.utfpr.ProjetoIDRAPI.search.request.filter.SearchFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

import static org.mockito.Mockito.*;

public class SearchHandlerTest {

    @Mock
    private JpaSpecificationExecutor<?> executorMock;

    private SearchHandler<?> searchHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(executorMock.findAll(any(Specification.class), any(Pageable.class))).thenReturn(null);
        searchHandler = new SearchHandler<>(executorMock);
    }

    @Test
    void testHandle() {
        SearchRequest searchRequest = new SearchRequest();
        searchHandler.handle(searchRequest);
        verify(executorMock).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void buildSpecificationTest() {
        List<SearchFilter> filters = List.of(mock(SearchFilter.class));
        Assertions.assertFalse(searchHandler.createSpecifications(filters).isEmpty());
    }
}
