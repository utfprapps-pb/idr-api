package br.gov.pr.idr.infra.inventory_resource.medication;

import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationID;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.medication.persistence.MedicationJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.medication.persistence.MedicationJPARepository;
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
@DisplayName("MedicationPostgresGateway")
class MedicationPostgresGatewayTest {

    @Mock
    MedicationJPARepository repository;

    @Mock
    MedicationJPAEntity entityMock;

    @InjectMocks
    MedicationPostgresGateway gateway;

    private Medication validMedication(final AnimalID animalId) {
        return Medication.create(animalId, ProductID.unique(), LocalDate.of(2024, 3, 10), "10ml",
                "Intramuscular");
    }

    @Test
    @DisplayName("deve salvar medicação e retornar agregado")
    void shouldSaveMedication() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validMedication(animalId));

        final var result = gateway.save(validMedication(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar medicação e retornar agregado")
    void shouldUpdateMedication() {
        final var animalId = AnimalID.unique();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validMedication(animalId));

        final var result = gateway.update(validMedication(animalId));

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com medicação quando encontrada por ID (roundtrip)")
    void shouldFindByIdWhenFound() {
        final var animalId = AnimalID.unique();
        final var medication = validMedication(animalId);
        final var id = medication.getId();
        when(repository.findByIdAndAnimalId(id.id(), animalId.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(medication);

        final var result = gateway.findByIdAndAnimalId(id, animalId);

        assertTrue(result.isPresent());
        assertEquals(medication.getAppliedDose(), result.get().getAppliedDose());
        assertEquals(animalId, result.get().getAnimalId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando medicação não é encontrada por ID")
    void shouldReturnEmptyWhenNotFoundById() {
        final var animalId = AnimalID.unique();
        final var id = MedicationID.unique();
        when(repository.findByIdAndAnimalId(id.id(), animalId.id())).thenReturn(Optional.empty());

        final var result = gateway.findByIdAndAnimalId(id, animalId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar medicação por ID (soft delete)")
    void shouldDeleteById() {
        final var id = MedicationID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("roundtrip: salvar e recuperar medicação mantém dados íntegros")
    void shouldRoundtripSaveAndFind() {
        final var animalId = AnimalID.unique();
        final var medication = validMedication(animalId);

        final var savedEntity = MedicationJPAEntity.from(medication);
        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findByIdAndAnimalId(medication.getId().id(), medication.getAnimalId().id())).thenReturn(Optional.of(savedEntity));

        gateway.save(medication);
        final var result = gateway.findByIdAndAnimalId(medication.getId(), medication.getAnimalId());

        assertTrue(result.isPresent());
        assertEquals(medication.getAppliedDose(), result.get().getAppliedDose());
        assertEquals(medication.getAnimalId(), result.get().getAnimalId());
        assertEquals(medication.getProductId(), result.get().getProductId());
        assertEquals(medication.getApplicationDate(), result.get().getApplicationDate());
        assertEquals(medication.getApplicationWay(), result.get().getApplicationWay());
    }

    @Test
    @DisplayName("search(animalId, query) deve retornar apenas medicações do animal informado, "
            + "escopando a consulta por animalId")
    void shouldScopeSearchByAnimalId() {
        final var animalA = AnimalID.unique();
        final var medicationFromAnimalA = validMedication(animalA);
        final var entityFromAnimalA = MedicationJPAEntity.from(medicationFromAnimalA);

        final var page = new PageImpl<>(List.of(entityFromAnimalA));
        when(repository.findAllByAnimalId(eq(animalA.id()), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "applicationDate", "asc");
        final var result = gateway.search(animalA, query);

        assertEquals(1, result.total());
        assertEquals(animalA, result.items().get(0).getAnimalId());

        final var animalIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(repository).findAllByAnimalId(animalIdCaptor.capture(), any(Pageable.class));
        assertEquals(animalA.id(), animalIdCaptor.getValue());

        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("search(animalId, query) não mistura medicações de animais diferentes")
    void shouldNotMixMedicationsFromDifferentAnimals() {
        final var animalB = AnimalID.unique();
        when(repository.findAllByAnimalId(eq(animalB.id()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var query = SearchQuery.from(0, 10, "", "applicationDate", "asc");
        final var resultForB = gateway.search(animalB, query);

        assertEquals(0, resultForB.total());
    }
}
