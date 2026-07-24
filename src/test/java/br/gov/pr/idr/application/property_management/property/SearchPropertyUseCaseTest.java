package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyCommand;
import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyUseCase;
import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchPropertyUseCase")
class SearchPropertyUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @Mock UserGateway userGateway;
    @InjectMocks SearchPropertyUseCase useCase;

    private static final SearchQuery QUERY = SearchQuery.from(0, 10, "", "name", "asc");

    @Test
    @DisplayName("Coordenação Geral enxerga todos os registros (escopo irrestrito)")
    void shouldResolveUnrestrictedScopeForCoordenacaoGeral() {
        final var user = userWith(UserRole.COORDENACAO_GERAL, Set.of(), Set.of());
        when(userGateway.findByUsername("coord")).thenReturn(Optional.of(user));
        when(propertyGateway.search(any(), any())).thenReturn(emptyPage());

        useCase.execute(SearchPropertyCommand.from("coord", QUERY));

        assertEquals(PropertySearchScope.Type.UNRESTRICTED, capturedScope().type());
    }

    @Test
    @DisplayName("Gerência Municipal enxerga apenas as cidades concedidas (escopo por localização)")
    void shouldResolveLocationScopeForGerenciaMunicipal() {
        final var cityId = UUID.randomUUID();
        final var user = userWith(UserRole.GERENCIA_MUNICIPAL, Set.of(), Set.of(cityId));
        when(userGateway.findByUsername("gm")).thenReturn(Optional.of(user));
        when(propertyGateway.search(any(), any())).thenReturn(emptyPage());

        useCase.execute(SearchPropertyCommand.from("gm", QUERY));

        final var scope = capturedScope();
        assertEquals(PropertySearchScope.Type.BY_LOCATION, scope.type());
        assertEquals(Set.of(CityID.from(cityId)), scope.cityIds());
        assertTrue(scope.regionIds().isEmpty());
    }

    @Test
    @DisplayName("Técnico enxerga apenas propriedades sob sua responsabilidade (escopo por técnico)")
    void shouldResolveTechnicianScopeForTecnico() {
        final var user = userWith(UserRole.TECNICO, Set.of(), Set.of());
        when(userGateway.findByUsername("tec")).thenReturn(Optional.of(user));
        when(propertyGateway.search(any(), any())).thenReturn(emptyPage());

        useCase.execute(SearchPropertyCommand.from("tec", QUERY));

        final var scope = capturedScope();
        assertEquals(PropertySearchScope.Type.BY_TECHNICIAN, scope.type());
        assertEquals(user.getId(), scope.technicianId());
    }

    @Test
    @DisplayName("deve retornar paginação de propriedades mapeada")
    void shouldReturnMappedPagination() {
        final var user = userWith(UserRole.COORDENACAO_GERAL, Set.of(), Set.of());
        when(userGateway.findByUsername(any())).thenReturn(Optional.of(user));
        when(propertyGateway.search(eq(QUERY), any()))
                .thenReturn(new Pagination<>(0, 10, 1L, List.of(stubProperty())));

        final var result = useCase.execute(SearchPropertyCommand.from("coord", QUERY));

        assertEquals(1L, result.total());
        assertEquals("Fazenda", result.items().getFirst().name());
    }

    @Test
    @DisplayName("deve lançar UserException quando usuário não é encontrado pelo username")
    void shouldThrowUserExceptionWhenUserNotFound() {
        when(userGateway.findByUsername("desconhecido")).thenReturn(Optional.empty());
        final var command = SearchPropertyCommand.from("desconhecido", QUERY);

        final var exception = assertThrows(UserException.class, () -> useCase.execute(command));

        assertEquals("Usuário com o username desconhecido não encontrado", exception.getMessage());
        org.mockito.Mockito.verifyNoInteractions(propertyGateway);
    }

    private PropertySearchScope capturedScope() {
        final var captor = ArgumentCaptor.forClass(PropertySearchScope.class);
        org.mockito.Mockito.verify(propertyGateway).search(any(), captor.capture());
        return captor.getValue();
    }

    private Pagination<Property> emptyPage() {
        return new Pagination<>(0, 10, 0L, List.of());
    }

    private User userWith(final UserRole role, final Set<UUID> regionIds, final Set<UUID> cityIds) {
        final var permission = Permission.create(role, false, regionIds, cityIds);
        return User.create(
                "Usuário", "user", Password.from("Senha@123", "Senha@123"), CPF.from("529.982.247-25"),
                "99999999", CityID.unique(), "80000000", "Rua", "10", "REG-1", "2020", Set.of(permission));
    }

    private Property stubProperty() {
        return Property.with(
                PropertyID.unique(), "Fazenda",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                ProducerID.from(UUID.randomUUID()), CityID.from(UUID.randomUUID()), List.of(), List.of(),
                0L, Instant.now()
        );
    }
}
