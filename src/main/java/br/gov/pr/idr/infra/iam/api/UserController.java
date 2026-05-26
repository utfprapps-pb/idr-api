package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.user.create.CreateUserCommand;
import br.gov.pr.idr.application.iam.user.create.CreateUserUseCase;
import br.gov.pr.idr.application.iam.user.retries.FindUserByUsernameUseCase;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserRequest;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserResponse;
import br.gov.pr.idr.infra.iam.user.models.retries.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

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
    public ResponseEntity<GetUserResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return ResponseEntity.noContent().build();

        final Object principal = authentication.getPrincipal();
        if (principal == null) return ResponseEntity.noContent().build();

        final String userName = principal.toString();
        if (userName == null) return ResponseEntity.noContent().build();

        final var output = findUserByUsernameUseCase.execute(userName);
        return ResponseEntity.ok(GetUserResponse.from(output));
    }

    @PostMapping("/search")
    public Pagination<GetUserResponse> search(@RequestBody SearchQuery searchQuery) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
