package br.gov.pr.idr.infra.livestock.animal;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPAEntity;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalPostgresGateway")
class AnimalPostgresGatewayTest {

    @Mock
    AnimalJPARepository repository;

    @Mock
    AnimalJPAEntity entityMock;

    @InjectMocks
    AnimalPostgresGateway gateway;

    private Animal validAnimal(final PropertyID propertyId) {
        return Animal.create(propertyId, null, BreedID.unique(), "Bovino", "Mimosa", "F", "Normal",
                LocalDate.of(2020, 1, 1), 30f, 180f, 220f, 3.5f);
    }

    @Test
    @DisplayName("deve salvar animal e retornar agregado")
    void shouldSaveAnimal() {
        final var propertyId = PropertyID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimal(propertyId));

        final var result = gateway.save(validAnimal(propertyId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar animal e retornar agregado")
    void shouldUpdateAnimal() {
        final var propertyId = PropertyID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimal(propertyId));

        final var result = gateway.update(validAnimal(propertyId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com animal quando encontrado por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var propertyId = PropertyID.unique();
        final var animal = validAnimal(propertyId);
        final var id = animal.getId();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(animal);

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(animal.getIdentifier(), result.get().getIdentifier());
        assertEquals(propertyId, result.get().getPropertyId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando animal não é encontrado por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var id = AnimalID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar animal por ID (soft delete)")
    void shouldDeleteById() {
        final var id = AnimalID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar animal mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var propertyId = PropertyID.unique();
        final var animal = validAnimal(propertyId);
        when(repository.save(any())).thenAnswer(invocation -> {
            final AnimalJPAEntity entity = invocation.getArgument(0);
            return entity;
        });

        final var savedEntity = AnimalJPAEntity.from(animal);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findById(animal.getId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(animal);
        final var result = gateway.findById(animal.getId());

        assertTrue(result.isPresent());
        assertEquals(animal.getIdentifier(), result.get().getIdentifier());
        assertEquals(animal.getPropertyId(), result.get().getPropertyId());
        assertEquals(animal.getBreedId(), result.get().getBreedId());
        assertTrue(result.get().getAnimalMotherId().isEmpty());
    }

    @Test
    @DisplayName("search(propertyId, query) deve retornar apenas animais da propriedade informada, " +
            "escopando a consulta por propertyId (prova da correção da falha de segurança)")
    void shouldScopeSearchByPropertyId() {
        final var propertyA = PropertyID.unique();
        final var animalFromPropertyA = validAnimal(propertyA);
        final var entityFromPropertyA = AnimalJPAEntity.from(animalFromPropertyA);

        final var page = new PageImpl<>(List.of(entityFromPropertyA));
        when(repository.findAllByPropertyIdAndIdentifierContainingIgnoreCase(eq(propertyA.id()), anyString(),
                any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "identifier", "asc");
        final var result = gateway.search(propertyA, query);

        assertEquals(1, result.total());
        assertEquals(propertyA, result.items().get(0).getPropertyId());

        final var propertyIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(repository).findAllByPropertyIdAndIdentifierContainingIgnoreCase(propertyIdCaptor.capture(),
                anyString(), any(Pageable.class));
        assertEquals(propertyA.id(), propertyIdCaptor.getValue());

        // A propriedade B nunca é consultada: o gateway delega exclusivamente para o propertyId informado,
        // nunca busca por "todos os animais" sem escopo de propriedade.
        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("findByPropertyIdAndIdentifier deve retornar o animal apenas quando pertence à propriedade informada")
    void shouldFindByPropertyIdAndIdentifier() {
        final var propertyId = PropertyID.unique();
        final var animal = validAnimal(propertyId);
        final var entity = AnimalJPAEntity.from(animal);
        when(repository.findByPropertyIdAndIdentifier(propertyId.id(), animal.getIdentifier()))
                .thenReturn(Optional.of(entity));

        final var result = gateway.findByPropertyIdAndIdentifier(propertyId, animal.getIdentifier());

        assertTrue(result.isPresent());
        assertEquals(propertyId, result.get().getPropertyId());
        assertEquals(animal.getIdentifier(), result.get().getIdentifier());
    }

    @Test
    @DisplayName("findByPropertyIdAndIdentifier deve retornar Optional vazio quando não encontrado na propriedade")
    void shouldReturnEmptyWhenIdentifierNotFoundInProperty() {
        final var propertyId = PropertyID.unique();
        when(repository.findByPropertyIdAndIdentifier(propertyId.id(), "inexistente"))
                .thenReturn(Optional.empty());

        final var result = gateway.findByPropertyIdAndIdentifier(propertyId, "inexistente");

        assertTrue(result.isEmpty());
    }
}
