package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.user.create.CreateUserCommand;
import br.gov.pr.idr.application.iam.user.create.CreateUserUseCase;
import br.gov.pr.idr.application.iam.user.retries.search.SearchUserUseCase;
import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserRequest;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserResponse;
import br.gov.pr.idr.infra.iam.user.models.retries.GetUserResponse;
import br.gov.pr.idr.infra.iam.user.models.retries.SearchUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final SearchUserUseCase searchUserUseCase;

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        final var command = CreateUserCommand.from(
                request.name(),
                request.username(),
                request.password(),
                request.confirmPassword(),
                request.cpf(),
                request.phone(),
                request.graduationYear(),
                request.professionalRegister(),
                request.cep(),
                request.street(),
                request.cityId(),
                request.houseNumber());
        final var output = this.createUserUseCase.execute(command);
        return ResponseEntity.ok(CreateUserResponse.from(output));
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> me(Authentication authentication) {
        if (authentication == null) return ResponseEntity.noContent().build();
        var username = authentication.getPrincipal();
        if (username == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(GetUserResponse.from(username.toString()));
    }

    @GetMapping("/search")
    public Pagination<SearchUserResponse> search(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "name") final String sort,
            @RequestParam(defaultValue = "asc") final String direction,
            @RequestParam(defaultValue = "true") final Boolean active) {
        final var query = SearchUserQuery.from(page, perPage, terms, sort, direction, active);
        return searchUserUseCase.execute(query)
                .map(SearchUserResponse::from);
    }

}
