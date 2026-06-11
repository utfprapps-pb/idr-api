package br.gov.pr.idr.infra.iam.security.jwt;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenCommand;
import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.user.models.auth.AuthLoginRequest;
import br.gov.pr.idr.infra.iam.user.models.auth.AuthLoginResponse;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
