package br.gov.pr.idr.infra.livestock.insemination;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.insemination.persistence.InseminationJPAEntity;
import br.gov.pr.idr.infra.livestock.insemination.persistence.InseminationJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InseminationPostgresGateway")
class InseminationPostgresGatewayTest {

    @Mock
    InseminationJPARepository repository;

    @Mock
    InseminationJPAEntity entityMock;

    @InjectMocks
    InseminationPostgresGateway gateway;

    private Insemination validInsemination(final AnimalID animalId) {
        return Insemination.create(animalId, LocalDate.of(2024, 3, 10), "Touro Teste");
    }

    @Test
    @DisplayName("deve salvar inseminação e retornar agregado")
    void shouldSaveInsemination() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validInsemination(animalId));

        final var result = gateway.save(validInsemination(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar inseminação e retornar agregado")
    void shouldUpdateInsemination() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validInsemination(animalId));

        final var result = gateway.update(validInsemination(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com inseminação quando encontrada por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var animalId = AnimalID.unique();
        final var insemination = validInsemination(animalId);
        final var id = insemination.getId();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(insemination);

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(insemination.getBull(), result.get().getBull());
        assertEquals(animalId, result.get().getAnimalId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando inseminação não é encontrada por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var id = InseminationID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar inseminação por ID (soft delete)")
    void shouldDeleteById() {
        final var id = InseminationID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar inseminação mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var animalId = AnimalID.unique();
        final var insemination = validInsemination(animalId);

        final var savedEntity = InseminationJPAEntity.from(insemination);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findById(insemination.getId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(insemination);
        final var result = gateway.findById(insemination.getId());

        assertTrue(result.isPresent());
        assertEquals(insemination.getBull(), result.get().getBull());
        assertEquals(insemination.getAnimalId(), result.get().getAnimalId());
        assertEquals(insemination.getIaDate(), result.get().getIaDate());
    }

    @Test
    @DisplayName("search(animalId, query) deve retornar apenas inseminações do animal informado, "
            + "escopando a consulta por animalId")
    void shouldScopeSearchByAnimalId() {
        final var animalA = AnimalID.unique();
        final var inseminationFromAnimalA = validInsemination(animalA);
        final var entityFromAnimalA = InseminationJPAEntity.from(inseminationFromAnimalA);

        final var page = new PageImpl<>(List.of(entityFromAnimalA));
        when(repository.findAllByAnimalId(eq(animalA.id()), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "iaDate", "asc");
        final var result = gateway.search(animalA, query);

        assertEquals(1, result.total());
        assertEquals(animalA, result.items().get(0).getAnimalId());

        final var animalIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(repository).findAllByAnimalId(animalIdCaptor.capture(), any(Pageable.class));
        assertEquals(animalA.id(), animalIdCaptor.getValue());

        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("search(animalId, query) não mistura inseminações de animais diferentes")
    void shouldNotMixInseminationsFromDifferentAnimals() {
        final var animalB = AnimalID.unique();
        when(repository.findAllByAnimalId(eq(animalB.id()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var query = SearchQuery.from(0, 10, "", "iaDate", "asc");
        final var resultForB = gateway.search(animalB, query);

        assertEquals(0, resultForB.total());
    }
}
