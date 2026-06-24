package br.gov.pr.idr.application.iam.user;

import br.gov.pr.idr.application.iam.user.retries.search.SearchUserOutput;
import br.gov.pr.idr.application.iam.user.retries.search.SearchUserUseCase;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.search.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchUserUseCase")
class SearchUserUseCaseTest {

    @Mock UserGateway userGateway;
    @InjectMocks SearchUserUseCase useCase;

    private User testUser() {
        return User.create("Carlos", "carlos", Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), null, CityID.unique(), null, null, null, null, null, Set.of());
    }

    @Test
    @DisplayName("deve retornar paginação mapeada de usuários")
    void shouldReturnMappedPagination() {
        final var user = testUser();
        final var pagination = new Pagination<>(0, 10, 1L, List.of(user));
        when(userGateway.search(any())).thenReturn(pagination);

        final var query = SearchUserQuery.from(0, 10, "Carlos", "name", "asc", null);
        final var result = useCase.execute(query);

        assertEquals(1L, result.total());
        assertEquals(1, result.items().size());
    }

    @Test
    @DisplayName("deve retornar paginação vazia quando não há resultados")
    void shouldReturnEmptyPagination() {
        final var empty = new Pagination<User>(0, 10, 0L, List.of());
        when(userGateway.search(any())).thenReturn(empty);

        final var query = SearchUserQuery.from(0, 10, "xyz", "name", "asc", null);
        final var result = useCase.execute(query);

        assertTrue(result.items().isEmpty());
        assertEquals(0L, result.total());
    }
}
