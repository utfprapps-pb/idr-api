package br.gov.pr.idr.infra.livestock.mastitis;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.mastitis.persistence.MastitisJPAEntity;
import br.gov.pr.idr.infra.livestock.mastitis.persistence.MastitisJPARepository;
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
@DisplayName("MastitisPostgresGateway")
class MastitisPostgresGatewayTest {

    @Mock
    MastitisJPARepository repository;

    @Mock
    MastitisJPAEntity entityMock;

    @InjectMocks
    MastitisPostgresGateway gateway;

    private Mastitis validMastitis(final AnimalID animalId) {
        return Mastitis.create(animalId, LocalDate.of(2024, 3, 10), "Clinica", "+", "Positivo", "Negativo",
                "Positivo", "Negativo");
    }

    @Test
    @DisplayName("deve salvar mastite e retornar agregado")
    void shouldSaveMastitis() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validMastitis(animalId));

        final var result = gateway.save(validMastitis(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar mastite e retornar agregado")
    void shouldUpdateMastitis() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validMastitis(animalId));

        final var result = gateway.update(validMastitis(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com mastite quando encontrada por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var animalId = AnimalID.unique();
        final var mastitis = validMastitis(animalId);
        final var id = mastitis.getId();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(mastitis);

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(mastitis.getMastitisType(), result.get().getMastitisType());
        assertEquals(animalId, result.get().getAnimalId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando mastite não é encontrada por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var id = MastitisID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar mastite por ID (soft delete)")
    void shouldDeleteById() {
        final var id = MastitisID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar mastite mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var animalId = AnimalID.unique();
        final var mastitis = validMastitis(animalId);

        final var savedEntity = MastitisJPAEntity.from(mastitis);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findById(mastitis.getId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(mastitis);
        final var result = gateway.findById(mastitis.getId());

        assertTrue(result.isPresent());
        assertEquals(mastitis.getMastitisType(), result.get().getMastitisType());
        assertEquals(mastitis.getAnimalId(), result.get().getAnimalId());
        assertEquals(mastitis.getDiagnoseDate(), result.get().getDiagnoseDate());
        assertEquals(mastitis.getCmtResult(), result.get().getCmtResult());
        assertEquals(mastitis.getAd(), result.get().getAd());
        assertEquals(mastitis.getAe(), result.get().getAe());
        assertEquals(mastitis.getPd(), result.get().getPd());
        assertEquals(mastitis.getPe(), result.get().getPe());
    }

    @Test
    @DisplayName("search(animalId, query) deve retornar apenas mastites do animal informado, "
            + "escopando a consulta por animalId")
    void shouldScopeSearchByAnimalId() {
        final var animalA = AnimalID.unique();
        final var mastitisFromAnimalA = validMastitis(animalA);
        final var entityFromAnimalA = MastitisJPAEntity.from(mastitisFromAnimalA);

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
    @DisplayName("search(animalId, query) não mistura mastites de animais diferentes")
    void shouldNotMixMastitisFromDifferentAnimals() {
        final var animalB = AnimalID.unique();
        when(repository.findAllByAnimalId(eq(animalB.id()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var query = SearchQuery.from(0, 10, "", "diagnoseDate", "asc");
        final var resultForB = gateway.search(animalB, query);

        assertEquals(0, resultForB.total());
    }
}
