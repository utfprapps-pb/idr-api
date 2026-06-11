package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.refresh_token.rotate.RotateRefreshTokenCommand;
import br.gov.pr.idr.application.iam.refresh_token.rotate.RotateRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import br.gov.pr.idr.infra.iam.security.config.JwtProperties;
import br.gov.pr.idr.infra.iam.user.models.auth.AuthLoginResponse;
import br.gov.pr.idr.infra.iam.user.models.auth.RefreshTokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final RotateRefreshTokenUseCase rotateRefreshTokenUseCase;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    @PostMapping("/refresh")
    public ResponseEntity<AuthLoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        final var command = RotateRefreshTokenCommand.from(request.token(),
                                                                        jwtProperties.refreshTokenExpirationDays());
        final var output = rotateRefreshTokenUseCase.execute(command);
        final var userDetails = userDetailsService.loadUserByUsername(output.username());
        return ResponseEntity.ok(AuthLoginResponse.from(
                jwtService.generateToken(userDetails),
                output.newRefreshToken()
        ));
    }
}
