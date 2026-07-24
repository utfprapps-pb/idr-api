package br.gov.pr.idr.infra.iam.security.jwt;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenCommand;
import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.user.models.auth.AuthLoginRequest;
import br.gov.pr.idr.infra.iam.user.models.auth.AuthLoginResponse;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.shared.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.List;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final IssueRefreshTokenUseCase issueRefreshTokenUseCase;
    private final long refreshTokenExpirationDays;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            ObjectMapper objectMapper,
            IssueRefreshTokenUseCase issueRefreshTokenUseCase,
            long refreshTokenExpirationDays
    ) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.issueRefreshTokenUseCase = issueRefreshTokenUseCase;
        this.refreshTokenExpirationDays = refreshTokenExpirationDays;
    }

    @Override
    @SneakyThrows
    @NullMarked
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        final var credentials = objectMapper.readValue(request.getInputStream(), AuthLoginRequest.class);
        final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(
                credentials.username(),
                credentials.password()
        );
        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    @SneakyThrows
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) {
        if (failed instanceof DisabledException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.FORBIDDEN.value(),
                    "Seu cadastro está inativo, solicite há um administrador para ativá-lo",
                    List.of()
            ));
            return;
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull FilterChain chain,
            Authentication authResult
    ) {
        final var userEntity = (UserJPAEntity) authResult.getPrincipal();
        assert userEntity != null;
        final var accessToken = jwtService.generateToken(userEntity);
        IssueRefreshTokenCommand command = IssueRefreshTokenCommand.from(userEntity.getId(),
                                                                      userEntity.getUsername(),
                                                                      refreshTokenExpirationDays);
        final var output = issueRefreshTokenUseCase.execute(command);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(),
                new AuthLoginResponse(accessToken, output.refreshToken()));
    }
}
