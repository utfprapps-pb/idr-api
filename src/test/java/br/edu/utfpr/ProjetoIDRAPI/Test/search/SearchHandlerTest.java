package br.edu.utfpr.ProjetoIDRAPI.Test.search;

import br.edu.utfpr.ProjetoIDRAPI.search.SearchHandler;
import br.edu.utfpr.ProjetoIDRAPI.search.request.SearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import static org.mockito.Mockito.*;

public class SearchHandlerTest {

    @Mock
    private JpaSpecificationExecutor<?> executorMock;

    private SearchHandler<?> searchHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(executorMock.findAll(any(Specification.class), any(Pageable.class))).thenReturn(null);
        searchHandler = new SearchHandler<>(executorMock);
    }

    @Test
    public void testHandle() {
        SearchRequest searchRequest = new SearchRequest();
        searchHandler.handle(searchRequest);
        verify(executorMock).findAll(any(Specification.class), any(Pageable.class));
    }
}
