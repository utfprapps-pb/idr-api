package br.gov.pr.idr.infra.property_management.synchronization;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.infra.iam.permission.persistence.PermissionJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SyncScopePostgresGateway")
class SyncScopePostgresGatewayTest {

    @Mock UserJPARepository userRepository;
    @Mock UserJPAEntity userEntityMock;
    @InjectMocks SyncScopePostgresGateway gateway;

    @Test
    @DisplayName("deve resolver escopo agregando regiões e cidades das permissões do técnico")
    void shouldResolveScopeByTechnician() {
        final var technicianId = UserID.unique();
        final var regionId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var permission = new PermissionJPAEntity(
                UUID.randomUUID(), UserRole.TECNICO, false, Set.of(regionId), Set.of(cityId));
        when(userRepository.findById(technicianId.id())).thenReturn(Optional.of(userEntityMock));
        when(userEntityMock.getUserPermissions()).thenReturn(Set.of(permission));

        final var result = gateway.resolveByTechnician(technicianId);

        assertEquals(Set.of(RegionID.from(regionId)), result.regionIds());
        assertEquals(Set.of(CityID.from(cityId)), result.cityIds());
    }

    @Test
    @DisplayName("deve retornar escopo vazio quando técnico não possui permissões")
    void shouldResolveEmptyScopeWhenNoPermissions() {
        final var technicianId = UserID.unique();
        when(userRepository.findById(technicianId.id())).thenReturn(Optional.of(userEntityMock));
        when(userEntityMock.getUserPermissions()).thenReturn(Set.of());

        final var result = gateway.resolveByTechnician(technicianId);

        assertTrue(result.regionIds().isEmpty());
        assertTrue(result.cityIds().isEmpty());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando técnico não existe")
    void shouldThrowNotFoundWhenTechnicianDoesNotExist() {
        final var technicianId = UserID.unique();
        when(userRepository.findById(technicianId.id())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> gateway.resolveByTechnician(technicianId));
    }
}
