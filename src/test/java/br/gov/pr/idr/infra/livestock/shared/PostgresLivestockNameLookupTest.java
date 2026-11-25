package br.gov.pr.idr.infra.livestock.shared;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository;
import br.gov.pr.idr.infra.livestock.animalsize.persistence.AnimalSizeJPARepository;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostgresLivestockNameLookup")
class PostgresLivestockNameLookupTest {

    @Mock BreedJPARepository breedRepository;
    @Mock AnimalSizeJPARepository animalSizeRepository;
    @Mock AnimalJPARepository animalRepository;
    @InjectMocks PostgresLivestockNameLookup lookup;

    @Test
    @DisplayName("breedNames busca só id e nome, numa única consulta")
    void shouldResolveBreedNamesWithOneProjectionQuery() {
        final var first = BreedID.unique();
        final var second = BreedID.unique();
        final var view = mock(BreedJPARepository.BreedNameView.class);
        when(view.getId()).thenReturn(first.id());
        when(view.getBreedName()).thenReturn("Gir");
        when(breedRepository.findByIdIn(List.of(first.id(), second.id()))).thenReturn(List.of(view));

        assertEquals(Map.of(first, "Gir"), lookup.breedNames(List.of(first, second)));
        verify(breedRepository, never()).findAllById(any());
    }

    @Test
    @DisplayName("animalIdentifiers busca só id e identificador, numa única consulta")
    void shouldResolveAnimalIdentifiersWithOneProjectionQuery() {
        final var id = AnimalID.unique();
        final var view = mock(AnimalJPARepository.AnimalIdentifierView.class);
        when(view.getId()).thenReturn(id.id());
        when(view.getIdentifier()).thenReturn("Mimosa");
        when(animalRepository.findByIdIn(List.of(id.id()))).thenReturn(List.of(view));

        assertEquals("Mimosa", lookup.animalIdentifier(id));
    }

    @Test
    @DisplayName("sizeNames busca só id e nome do porte, numa única consulta")
    void shouldResolveSizeNamesWithOneProjectionQuery() {
        final var id = AnimalSizeID.unique();
        final var view = mock(AnimalSizeJPARepository.AnimalSizeNameView.class);
        when(view.getId()).thenReturn(id.id());
        when(view.getSizeName()).thenReturn("Grande");
        when(animalSizeRepository.findByIdIn(List.of(id.id()))).thenReturn(List.of(view));

        assertEquals("Grande", lookup.sizeName(id));
    }

    @Test
    @DisplayName("sizeName de id nulo devolve nulo sem consultar o banco")
    void shouldReturnNullSizeNameForNullId() {
        assertNull(lookup.sizeName(null));
        verifyNoInteractions(animalSizeRepository);
    }

    @Test
    @DisplayName("lista vazia não consulta o banco (contrato da porta)")
    void shouldNotQueryForEmptyInput() {
        assertTrue(lookup.breedNames(List.of()).isEmpty());
        assertTrue(lookup.sizeNames(List.of()).isEmpty());
        assertTrue(lookup.animalIdentifiers(List.of()).isEmpty());
        verifyNoInteractions(breedRepository, animalSizeRepository, animalRepository);
    }
}
