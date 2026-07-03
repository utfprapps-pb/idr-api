package br.gov.pr.idr.application.property_management.producer;

import br.gov.pr.idr.application.property_management.producer.retrieve.search.SearchProducerUseCase;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchProducerUseCase")
class SearchProducerUseCaseTest {

    @Mock ProducerGateway producerGateway;
    @InjectMocks SearchProducerUseCase useCase;

    @Test
    @DisplayName("deve retornar paginação de produtores mapeada")
    void shouldReturnMappedPagination() {
        final var producer = Producer.create("Agricultor", CPF.from("529.982.247-25"));
        final var pagination = new Pagination<>(0, 10, 1L, List.of(producer));
        when(producerGateway.search(any())).thenReturn(pagination);

        final var result = useCase.execute(SearchQuery.from(0, 10, "", "name", "asc"));

        assertEquals(1L, result.total());
        assertEquals(1, result.items().size());
    }

    @Test
    @DisplayName("deve retornar paginação vazia quando não há produtores")
    void shouldReturnEmptyPagination() {
        final var empty = new Pagination<Producer>(0, 10, 0L, List.of());
        when(producerGateway.search(any())).thenReturn(empty);

        final var result = useCase.execute(SearchQuery.from(0, 10, "", "name", "asc"));

        assertTrue(result.items().isEmpty());
    }
}
