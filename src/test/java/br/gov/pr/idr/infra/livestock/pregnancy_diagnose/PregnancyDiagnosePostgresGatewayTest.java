package br.gov.pr.idr.infra.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.persistence.PregnancyDiagnoseJPAEntity;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.persistence.PregnancyDiagnoseJPARepository;
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
@DisplayName("PregnancyDiagnosePostgresGateway")
class PregnancyDiagnosePostgresGatewayTest {

    @Mock
    PregnancyDiagnoseJPARepository repository;

    @Mock
    PregnancyDiagnoseJPAEntity entityMock;

    @InjectMocks
    PregnancyDiagnosePostgresGateway gateway;

    private PregnancyDiagnose validPregnancyDiagnose(final AnimalID animalId) {
        return PregnancyDiagnose.create(animalId, LocalDate.of(2024, 3, 10), LocalDate.of(2024, 1, 10));
    }

    @Test
    @DisplayName("deve salvar diagnóstico de gestação e retornar agregado")
    void shouldSavePregnancyDiagnose() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validPregnancyDiagnose(animalId));

        final var result = gateway.save(validPregnancyDiagnose(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar diagnóstico de gestação e retornar agregado")
    void shouldUpdatePregnancyDiagnose() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validPregnancyDiagnose(animalId));

        final var result = gateway.update(validPregnancyDiagnose(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com diagnóstico quando encontrado por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var animalId = AnimalID.unique();
        final var pregnancyDiagnose = validPregnancyDiagnose(animalId);
        final var id = pregnancyDiagnose.getId();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(pregnancyDiagnose);

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(pregnancyDiagnose.getLastIa(), result.get().getLastIa());
        assertEquals(animalId, result.get().getAnimalId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando diagnóstico não é encontrado por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var id = PregnancyDiagnoseID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar diagnóstico de gestação por ID (soft delete)")
    void shouldDeleteById() {
        final var id = PregnancyDiagnoseID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar diagnóstico mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var animalId = AnimalID.unique();
        final var pregnancyDiagnose = validPregnancyDiagnose(animalId);

        final var savedEntity = PregnancyDiagnoseJPAEntity.from(pregnancyDiagnose);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findById(pregnancyDiagnose.getId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(pregnancyDiagnose);
        final var result = gateway.findById(pregnancyDiagnose.getId());

        assertTrue(result.isPresent());
        assertEquals(pregnancyDiagnose.getAnimalId(), result.get().getAnimalId());
        assertEquals(pregnancyDiagnose.getDiagnoseDate(), result.get().getDiagnoseDate());
        assertEquals(pregnancyDiagnose.getLastIa(), result.get().getLastIa());
    }

    @Test
    @DisplayName("search(animalId, query) deve retornar apenas diagnósticos do animal informado, "
            + "escopando a consulta por animalId")
    void shouldScopeSearchByAnimalId() {
        final var animalA = AnimalID.unique();
        final var pregnancyDiagnoseFromAnimalA = validPregnancyDiagnose(animalA);
        final var entityFromAnimalA = PregnancyDiagnoseJPAEntity.from(pregnancyDiagnoseFromAnimalA);

        final var page = new PageImpl<>(List.of(entityFromAnimalA));
        when(repository.findAllByAnimalId(eq(animalA.id()), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "diagnoseDate", "asc");
        final var result = gateway.search(animalA, query);

        assertEquals(1, result.total());
        assertEquals(animalA, result.items().get(0).getAnimalId());

        final var animalIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(repository).findAllByAnimalId(animalIdCaptor.capture(), any(Pageable.class));
        assertEquals(animalA.id(), animalIdCaptor.getValue());

        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("search(animalId, query) não mistura diagnósticos de animais diferentes")
    void shouldNotMixPregnancyDiagnoseFromDifferentAnimals() {
        final var animalB = AnimalID.unique();
        when(repository.findAllByAnimalId(eq(animalB.id()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var query = SearchQuery.from(0, 10, "", "diagnoseDate", "asc");
        final var resultForB = gateway.search(animalB, query);

        assertEquals(0, resultForB.total());
    }
}
