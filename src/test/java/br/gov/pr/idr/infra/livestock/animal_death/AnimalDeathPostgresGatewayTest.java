package br.gov.pr.idr.infra.livestock.animal_death;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_death.persistence.AnimalDeathJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_death.persistence.AnimalDeathJPARepository;
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
@DisplayName("AnimalDeathPostgresGateway")
class AnimalDeathPostgresGatewayTest {

    @Mock
    AnimalDeathJPARepository repository;

    @Mock
    AnimalDeathJPAEntity entityMock;

    @InjectMocks
    AnimalDeathPostgresGateway gateway;

    private AnimalDeath validAnimalDeath(final AnimalID animalId) {
        return AnimalDeath.create(animalId, "Picada de cobra", LocalDate.of(2024, 3, 10));
    }

    @Test
    @DisplayName("deve salvar morte e retornar agregado")
    void shouldSaveAnimalDeath() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalDeath(animalId));

        final var result = gateway.save(validAnimalDeath(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar morte e retornar agregado")
    void shouldUpdateAnimalDeath() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalDeath(animalId));

        final var result = gateway.update(validAnimalDeath(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com morte quando encontrada por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var animalId = AnimalID.unique();
        final var animalDeath = validAnimalDeath(animalId);
        final var id = animalDeath.getId();
        when(repository.findByIdAndAnimalId(id.id(), animalId.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(animalDeath);

        final var result = gateway.findByIdAndAnimalId(id, animalId);

        assertTrue(result.isPresent());
        assertEquals(animalDeath.getReason(), result.get().getReason());
        assertEquals(animalId, result.get().getAnimalId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando morte não é encontrada por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var animalId = AnimalID.unique();
        final var id = AnimalDeathID.unique();
        when(repository.findByIdAndAnimalId(id.id(), animalId.id())).thenReturn(Optional.empty());

        final var result = gateway.findByIdAndAnimalId(id, animalId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar morte por ID (soft delete)")
    void shouldDeleteById() {
        final var id = AnimalDeathID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar morte mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var animalId = AnimalID.unique();
        final var animalDeath = validAnimalDeath(animalId);

        final var savedEntity = AnimalDeathJPAEntity.from(animalDeath);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findByIdAndAnimalId(animalDeath.getId().id(), animalDeath.getAnimalId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(animalDeath);
        final var result = gateway.findByIdAndAnimalId(animalDeath.getId(), animalDeath.getAnimalId());

        assertTrue(result.isPresent());
        assertEquals(animalDeath.getAnimalId(), result.get().getAnimalId());
        assertEquals(animalDeath.getReason(), result.get().getReason());
        assertEquals(animalDeath.getDeathDate(), result.get().getDeathDate());
    }

    @Test
    @DisplayName("search(animalId, query) deve retornar apenas mortes do animal informado, "
            + "escopando a consulta por animalId")
    void shouldScopeSearchByAnimalId() {
        final var animalA = AnimalID.unique();
        final var animalDeathFromAnimalA = validAnimalDeath(animalA);
        final var entityFromAnimalA = AnimalDeathJPAEntity.from(animalDeathFromAnimalA);

        final var page = new PageImpl<>(List.of(entityFromAnimalA));
        when(repository.findAllByAnimalId(eq(animalA.id()), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "deathDate", "asc");
        final var result = gateway.search(animalA, query);

        assertEquals(1, result.total());
        assertEquals(animalA, result.items().get(0).getAnimalId());

        final var animalIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(repository).findAllByAnimalId(animalIdCaptor.capture(), any(Pageable.class));
        assertEquals(animalA.id(), animalIdCaptor.getValue());

        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("search(animalId, query) não mistura mortes de animais diferentes")
    void shouldNotMixAnimalDeathFromDifferentAnimals() {
        final var animalB = AnimalID.unique();
        when(repository.findAllByAnimalId(eq(animalB.id()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var query = SearchQuery.from(0, 10, "", "deathDate", "asc");
        final var resultForB = gateway.search(animalB, query);

        assertEquals(0, resultForB.total());
    }

    @Test
    @DisplayName("existsByAnimalId delega ao repositório pelo UUID do animal")
    void shouldTellWhetherAnimalAlreadyHasDeath() {
        final var animalId = AnimalID.unique();
        when(repository.existsByAnimalId(animalId.id())).thenReturn(true);

        assertTrue(gateway.existsByAnimalId(animalId));
    }
}
