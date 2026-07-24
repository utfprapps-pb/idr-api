package br.gov.pr.idr.infra.property_management.producer;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPAEntity;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProducerPostgresGateway")
class ProducerPostgresGatewayTest {

    @Mock ProducerJPARepository repository;
    @Mock ProducerJPAEntity entityMock;
    @InjectMocks ProducerPostgresGateway gateway;

    private static final String VALID_CPF = "529.982.247-25";

    private Producer validProducer() {
        return Producer.create("Agricultor", CPF.from(VALID_CPF));
    }

    @Test
    @DisplayName("deve salvar produtor e retornar domínio")
    void shouldSaveProducer() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toDomain()).thenReturn(validProducer());

        final var result = gateway.save(validProducer());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar produtor e retornar domínio")
    void shouldUpdateProducer() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toDomain()).thenReturn(validProducer());

        final var result = gateway.update(validProducer());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com produtor quando encontrado por ID")
    void shouldFindByIdWhenFound() {
        final var id = ProducerID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toDomain()).thenReturn(validProducer());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando não encontrado")
    void shouldReturnEmptyWhenNotFound() {
        final var id = ProducerID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = ProducerID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve verificar existência por CPF")
    void shouldCheckExistsByCpf() {
        when(repository.existsByCpf(anyString())).thenReturn(false);

        assertFalse(gateway.existsByCpf(CPF.from(VALID_CPF)));
    }

    @Test
    @DisplayName("deve retornar Optional com produtor quando encontrado por CPF")
    void shouldFindByCpfWhenFound() {
        when(repository.findByCpf(VALID_CPF)).thenReturn(Optional.of(entityMock));
        when(entityMock.toDomain()).thenReturn(validProducer());

        final var result = gateway.findByCpf(CPF.from(VALID_CPF));

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando não encontrado por CPF")
    void shouldReturnEmptyWhenNotFoundByCpf() {
        when(repository.findByCpf(VALID_CPF)).thenReturn(Optional.empty());

        final var result = gateway.findByCpf(CPF.from(VALID_CPF));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar paginação na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.search(any(), any(Pageable.class))).thenReturn(page);
        when(entityMock.toDomain()).thenReturn(validProducer());

        final var query = SearchQuery.from(0, 10, "", "name", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }

    @Test
    @DisplayName("deve tratar terms nulo como string vazia na busca")
    void shouldTreatNullTermsAsEmptyOnSearch() {
        final var page = new PageImpl<ProducerJPAEntity>(List.of());
        when(repository.search(eq(""), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, null, "name", "asc");
        final var result = gateway.search(query);

        assertEquals(0, result.total());
        verify(repository).search(eq(""), any(Pageable.class));
    }

    @Test
    @DisplayName("deve remover espaços em branco dos terms na busca")
    void shouldTrimTermsOnSearch() {
        final var page = new PageImpl<ProducerJPAEntity>(List.of());
        when(repository.search(eq("Agricultor"), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "  Agricultor  ", "name", "asc");
        gateway.search(query);

        verify(repository).search(eq("Agricultor"), any(Pageable.class));
    }
}
