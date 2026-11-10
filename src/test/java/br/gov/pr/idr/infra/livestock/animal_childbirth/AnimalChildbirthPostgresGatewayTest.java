package br.gov.pr.idr.infra.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import java.math.BigDecimal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_childbirth.persistence.AnimalChildbirthJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_childbirth.persistence.AnimalChildbirthJPARepository;
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
@DisplayName("AnimalChildbirthPostgresGateway")
class AnimalChildbirthPostgresGatewayTest {

    @Mock
    AnimalChildbirthJPARepository repository;

    @Mock
    AnimalChildbirthJPAEntity entityMock;

    @InjectMocks
    AnimalChildbirthPostgresGateway gateway;

    private AnimalChildbirth validAnimalChildbirth(final AnimalID animalId) {
        return AnimalChildbirth.create(animalId, LocalDate.of(2024, 3, 10), ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE, BreedID.unique(), null);
    }

    @Test
    @DisplayName("deve salvar parto e retornar agregado")
    void shouldSaveAnimalChildbirth() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalChildbirth(animalId));

        final var result = gateway.save(validAnimalChildbirth(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar parto e retornar agregado")
    void shouldUpdateAnimalChildbirth() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalChildbirth(animalId));

        final var result = gateway.update(validAnimalChildbirth(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com parto quando encontrado por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var animalId = AnimalID.unique();
        final var animalChildbirth = validAnimalChildbirth(animalId);
        final var id = animalChildbirth.getId();
        when(repository.findByIdAndAnimalId(id.id(), animalId.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(animalChildbirth);

        final var result = gateway.findByIdAndAnimalId(id, animalId);

        assertTrue(result.isPresent());
        assertEquals(animalChildbirth.getChildbirthDate(), result.get().getChildbirthDate());
        assertEquals(animalId, result.get().getAnimalId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando parto não é encontrado por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var animalId = AnimalID.unique();
        final var id = AnimalChildbirthID.unique();
        when(repository.findByIdAndAnimalId(id.id(), animalId.id())).thenReturn(Optional.empty());

        final var result = gateway.findByIdAndAnimalId(id, animalId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar parto por ID (soft delete)")
    void shouldDeleteById() {
        final var id = AnimalChildbirthID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar parto mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var animalId = AnimalID.unique();
        final var animalChildbirth = validAnimalChildbirth(animalId);

        final var savedEntity = AnimalChildbirthJPAEntity.from(animalChildbirth);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findByIdAndAnimalId(animalChildbirth.getId().id(), animalChildbirth.getAnimalId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(animalChildbirth);
        final var result = gateway.findByIdAndAnimalId(animalChildbirth.getId(), animalChildbirth.getAnimalId());

        assertTrue(result.isPresent());
        assertEquals(animalChildbirth.getAnimalId(), result.get().getAnimalId());
        assertEquals(animalChildbirth.getChildbirthDate(), result.get().getChildbirthDate());
    }

    @Test
    @DisplayName("search(animalId, query) deve retornar apenas partos do animal informado, "
            + "escopando a consulta por animalId")
    void shouldScopeSearchByAnimalId() {
        final var animalA = AnimalID.unique();
        final var animalChildbirthFromAnimalA = validAnimalChildbirth(animalA);
        final var entityFromAnimalA = AnimalChildbirthJPAEntity.from(animalChildbirthFromAnimalA);

        final var page = new PageImpl<>(List.of(entityFromAnimalA));
        when(repository.findAllByAnimalId(eq(animalA.id()), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "childbirthDate", "asc");
        final var result = gateway.search(animalA, query);

        assertEquals(1, result.total());
        assertEquals(animalA, result.items().get(0).getAnimalId());

        final var animalIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(repository).findAllByAnimalId(animalIdCaptor.capture(), any(Pageable.class));
        assertEquals(animalA.id(), animalIdCaptor.getValue());

        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("search(animalId, query) não mistura partos de animais diferentes")
    void shouldNotMixAnimalChildbirthFromDifferentAnimals() {
        final var animalB = AnimalID.unique();
        when(repository.findAllByAnimalId(eq(animalB.id()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var query = SearchQuery.from(0, 10, "", "childbirthDate", "asc");
        final var resultForB = gateway.search(animalB, query);

        assertEquals(0, resultForB.total());
    }


    @Test
    @DisplayName("entidade JPA preserva enums, raça e bezerro no roundtrip from/toAggregate")
    void shouldKeepAllFieldsThroughJpaEntityRoundtrip() {
        final var calfId = AnimalID.unique();
        final var original = AnimalChildbirth.create(AnimalID.unique(), LocalDate.of(2024, 3, 10),
                ChildbirthGender.MALE, new BigDecimal("41.2"), ChildbirthCondition.DEAD, BreedID.unique(), calfId);

        final var roundtrip = AnimalChildbirthJPAEntity.from(original).toAggregate();

        assertEquals(ChildbirthGender.MALE, roundtrip.getGender());
        assertEquals(ChildbirthCondition.DEAD, roundtrip.getCondition());
        assertEquals(new BigDecimal("41.2"), roundtrip.getWeight());
        assertEquals(original.getBreedId(), roundtrip.getBreedId());
        assertEquals(Optional.of(calfId), roundtrip.getCalfId());
    }
}
