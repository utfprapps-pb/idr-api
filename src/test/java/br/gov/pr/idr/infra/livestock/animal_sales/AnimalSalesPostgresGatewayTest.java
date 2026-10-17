package br.gov.pr.idr.infra.livestock.animal_sales;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesID;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_sales.persistence.AnimalSalesJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_sales.persistence.AnimalSalesJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalSalesPostgresGateway")
class AnimalSalesPostgresGatewayTest {

    @Mock
    AnimalSalesJPARepository repository;

    @Mock
    AnimalSalesJPAEntity entityMock;

    @InjectMocks
    AnimalSalesPostgresGateway gateway;

    private AnimalSales validAnimalSales(final AnimalID animalId) {
        return AnimalSales.create(animalId, LocalDate.of(2024, 3, 10), Reason.VOLUNTARIA,
                BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450));
    }

    @Test
    @DisplayName("deve salvar venda e retornar agregado")
    void shouldSaveAnimalSales() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalSales(animalId));

        final var result = gateway.save(validAnimalSales(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar venda e retornar agregado")
    void shouldUpdateAnimalSales() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalSales(animalId));

        final var result = gateway.update(validAnimalSales(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com venda quando encontrada por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var animalId = AnimalID.unique();
        final var animalSales = validAnimalSales(animalId);
        final var id = animalSales.getId();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(animalSales);

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(animalSales.getValue(), result.get().getValue());
        assertEquals(animalId, result.get().getAnimalId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando venda não é encontrada por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var id = AnimalSalesID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar venda por ID (soft delete)")
    void shouldDeleteById() {
        final var id = AnimalSalesID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar venda mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var animalId = AnimalID.unique();
        final var animalSales = validAnimalSales(animalId);

        final var savedEntity = AnimalSalesJPAEntity.from(animalSales);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findById(animalSales.getId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(animalSales);
        final var result = gateway.findById(animalSales.getId());

        assertTrue(result.isPresent());
        assertEquals(animalSales.getAnimalId(), result.get().getAnimalId());
        assertEquals(animalSales.getDateSale(), result.get().getDateSale());
        assertEquals(animalSales.getReason(), result.get().getReason());
        assertEquals(animalSales.getValue(), result.get().getValue());
        assertEquals(animalSales.getDestination(), result.get().getDestination());
    }

    @Test
    @DisplayName("search(animalId, query) deve retornar apenas vendas do animal informado, "
            + "escopando a consulta por animalId")
    void shouldScopeSearchByAnimalId() {
        final var animalA = AnimalID.unique();
        final var animalSalesFromAnimalA = validAnimalSales(animalA);
        final var entityFromAnimalA = AnimalSalesJPAEntity.from(animalSalesFromAnimalA);

        final var page = new PageImpl<>(List.of(entityFromAnimalA));
        when(repository.findAllByAnimalId(eq(animalA.id()), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "dateSale", "asc");
        final var result = gateway.search(animalA, query);

        assertEquals(1, result.total());
        assertEquals(animalA, result.items().get(0).getAnimalId());

        final var animalIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(repository).findAllByAnimalId(animalIdCaptor.capture(), any(Pageable.class));
        assertEquals(animalA.id(), animalIdCaptor.getValue());

        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("search(animalId, query) não mistura vendas de animais diferentes")
    void shouldNotMixAnimalSalesFromDifferentAnimals() {
        final var animalB = AnimalID.unique();
        when(repository.findAllByAnimalId(eq(animalB.id()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var query = SearchQuery.from(0, 10, "", "dateSale", "asc");
        final var resultForB = gateway.search(animalB, query);

        assertEquals(0, resultForB.total());
    }

    @Test
    @DisplayName("entidade JPA preserva o peso no roundtrip from/toAggregate")
    void shouldKeepWeightThroughJpaEntityRoundtrip() {
        final var original = validAnimalSales(AnimalID.unique());

        final var roundtrip = AnimalSalesJPAEntity.from(original).toAggregate();

        assertEquals(BigDecimal.valueOf(450), roundtrip.getWeight());
        assertEquals(original.getValue(), roundtrip.getValue());
    }
}
