package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.user.create.CreateUserCommand;
import br.gov.pr.idr.application.iam.user.create.CreateUserUseCase;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserRequest;
import br.gov.pr.idr.infra.iam.user.models.create.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

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

}
