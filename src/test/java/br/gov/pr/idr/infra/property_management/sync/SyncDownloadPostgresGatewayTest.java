package br.gov.pr.idr.infra.property_management.sync;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.infra.iam.permission.persistence.PermissionJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPAEntity;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPARepository;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPAEntity;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SyncDownloadPostgresGateway")
class SyncDownloadPostgresGatewayTest {

    @Mock UserJPARepository userRepository;
    @Mock CityJPARepository cityRepository;
    @Mock RegionJPARepository regionRepository;
    @Mock ProducerJPARepository producerRepository;
    @InjectMocks SyncDownloadPostgresGateway gateway;

    @Mock UserJPAEntity userEntityMock;
    @Mock PermissionJPAEntity permissionEntityMock;
    @Mock RegionJPAEntity regionEntityMock;
    @Mock CityJPAEntity cityEntityMock;
    @Mock ProducerJPAEntity producerEntityMock;

    @Test
    @DisplayName("deve lançar NotFoundException quando técnico não for encontrado")
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        final var technicianId = UserID.unique();
        when(userRepository.findById(technicianId.id())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> gateway.findByTechnicianId(technicianId));
    }

    @Test
    @DisplayName("deve retornar snapshot vazio quando técnico não possui permissões de região")
    void shouldReturnEmptySnapshotWhenUserHasNoRegionPermissions() {
        final var technicianId = UserID.unique();
        when(userRepository.findById(technicianId.id())).thenReturn(Optional.of(userEntityMock));
        when(userEntityMock.getUserPermissions()).thenReturn(Set.of(permissionEntityMock));
        when(permissionEntityMock.getRegionIds()).thenReturn(Set.of());

        final var result = gateway.findByTechnicianId(technicianId);

        assertTrue(result.regions().isEmpty());
        assertTrue(result.cities().isEmpty());
        assertTrue(result.producers().isEmpty());
        verifyNoInteractions(regionRepository, cityRepository, producerRepository);
    }

    @Test
    @DisplayName("deve retornar produtores vazios quando nenhuma cidade for encontrada")
    void shouldReturnEmptyProducersWhenNoCitiesFound() {
        final var technicianId = UserID.unique();
        final var regionId = UUID.randomUUID();
        when(userRepository.findById(technicianId.id())).thenReturn(Optional.of(userEntityMock));
        when(userEntityMock.getUserPermissions()).thenReturn(Set.of(permissionEntityMock));
        when(permissionEntityMock.getRegionIds()).thenReturn(Set.of(regionId));
        when(regionRepository.findAllByIdIn(Set.of(regionId))).thenReturn(List.of());
        when(cityRepository.findAllByRegionIdIn(Set.of(regionId))).thenReturn(List.of());

        final var result = gateway.findByTechnicianId(technicianId);

        assertTrue(result.regions().isEmpty());
        assertTrue(result.cities().isEmpty());
        assertTrue(result.producers().isEmpty());
        verify(producerRepository, never()).findAllByCityIds(any());
    }

    @Test
    @DisplayName("deve retornar snapshot completo quando técnico possui permissões de região")
    void shouldReturnFullSnapshotWhenUserHasRegionPermissions() {
        final var technicianId = UserID.unique();
        final var regionId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var producerId = UUID.randomUUID();
        final var updatedAt = Instant.now();

        when(userRepository.findById(technicianId.id())).thenReturn(Optional.of(userEntityMock));
        when(userEntityMock.getUserPermissions()).thenReturn(Set.of(permissionEntityMock));
        when(permissionEntityMock.getRegionIds()).thenReturn(Set.of(regionId));

        when(regionEntityMock.getId()).thenReturn(regionId);
        when(regionEntityMock.getDescription()).thenReturn("Região Sul");
        when(regionRepository.findAllByIdIn(Set.of(regionId))).thenReturn(List.of(regionEntityMock));

        when(cityEntityMock.getId()).thenReturn(cityId);
        when(cityEntityMock.getName()).thenReturn("Curitiba");
        when(cityEntityMock.getState()).thenReturn(State.PR);
        when(cityRepository.findAllByRegionIdIn(Set.of(regionId))).thenReturn(List.of(cityEntityMock));

        when(producerEntityMock.getId()).thenReturn(producerId);
        when(producerEntityMock.getName()).thenReturn("Agricultor");
        when(producerEntityMock.getCpf()).thenReturn("52998224725");
        when(producerEntityMock.getVersion()).thenReturn(1L);
        when(producerEntityMock.getUpdatedAt()).thenReturn(updatedAt);
        when(producerRepository.findAllByCityIds(Set.of(cityId))).thenReturn(List.of(producerEntityMock));

        final var result = gateway.findByTechnicianId(technicianId);

        assertEquals(1, result.regions().size());
        assertEquals(regionId, result.regions().get(0).id());
        assertEquals("Região Sul", result.regions().get(0).name());

        assertEquals(1, result.cities().size());
        assertEquals(cityId, result.cities().get(0).id());
        assertEquals("Curitiba", result.cities().get(0).name());
        assertEquals(State.PR, result.cities().get(0).state());

        assertEquals(1, result.producers().size());
        assertEquals(producerId, result.producers().get(0).id());
        assertEquals("Agricultor", result.producers().get(0).name());
        assertEquals("52998224725", result.producers().get(0).cpf());
        assertEquals(1L, result.producers().get(0).version());
        assertEquals(updatedAt, result.producers().get(0).updatedAt());
    }
}
