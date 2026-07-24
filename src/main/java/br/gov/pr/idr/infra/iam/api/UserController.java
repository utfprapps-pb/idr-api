package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.user.create.CreateUserCommand;
import br.gov.pr.idr.application.iam.user.create.CreateUserUseCase;
import br.gov.pr.idr.application.iam.user.retries.find.FindUserByIdUseCase;
import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameUseCase;
import br.gov.pr.idr.application.iam.user.retries.permissions.GetUserPermissionsUseCase;
import br.gov.pr.idr.application.iam.user.retries.search.SearchUserUseCase;
import br.gov.pr.idr.application.iam.user.update.ToggleUserActiveUseCase;
import br.gov.pr.idr.application.iam.user.update.UpdateUserPermissionsCommand;
import br.gov.pr.idr.application.iam.user.update.UpdateUserPermissionsUseCase;
import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserRequest;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserResponse;
import br.gov.pr.idr.infra.iam.user.models.retries.GetUserByIdResponse;
import br.gov.pr.idr.infra.iam.user.models.retries.GetUserPermissionsResponse;
import br.gov.pr.idr.infra.iam.user.models.retries.GetUserResponse;
import br.gov.pr.idr.infra.iam.user.models.retries.SearchUserResponse;
import br.gov.pr.idr.infra.iam.user.models.update.UpdateUserPermissionsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final SearchUserUseCase searchUserUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final GetUserPermissionsUseCase getUserPermissionsUseCase;
    private final UpdateUserPermissionsUseCase updateUserPermissionsUseCase;
    private final ToggleUserActiveUseCase toggleUserActiveUseCase;

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
        final var username = authentication.getPrincipal();
        if (username == null) return ResponseEntity.noContent().build();
        final var user = findUserByUsernameUseCase.execute(username.toString());
        final var role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
        return ResponseEntity.ok(GetUserResponse.from(user.name(), role));
    }

    @GetMapping("/search")
    public Pagination<SearchUserResponse> search(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "name") final String sort,
            @RequestParam(defaultValue = "asc") final String direction,
            @RequestParam(required = false) final Boolean active) {
        final var query = SearchUserQuery.from(page, perPage, terms, sort, direction, active);
        return searchUserUseCase.execute(query)
                .map(SearchUserResponse::from);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserByIdResponse> findById(@PathVariable final UUID id) {
        final var output = findUserByIdUseCase.execute(id);
        return ResponseEntity.ok(GetUserByIdResponse.from(output));
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<GetUserPermissionsResponse> getPermissions(@PathVariable final UUID id) {
        final var output = getUserPermissionsUseCase.execute(id);
        return ResponseEntity.ok(GetUserPermissionsResponse.from(output));
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<Void> updatePermissions(
            @PathVariable final UUID id,
            @RequestBody final UpdateUserPermissionsRequest request) {
        final var command = UpdateUserPermissionsCommand.from(
                id,
                request.role(),
                request.readOnly(),
                request.regionIds(),
                request.cityIds()
        );
        updateUserPermissionsUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> toggleActive(@PathVariable final UUID id) {
        toggleUserActiveUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
