package br.gov.pr.idr.infra.iam.security.config;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.security.jwt.JwtAuthenticationFilter;
import br.gov.pr.idr.infra.iam.security.jwt.JwtAuthorizationFilter;
import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final IssueRefreshTokenUseCase issueRefreshTokenUseCase;

    public SecurityConfig(
            JwtService jwtService,
            JwtProperties jwtProperties,
            UserDetailsService userDetailsService,
            IssueRefreshTokenUseCase issueRefreshTokenUseCase
    ) {
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
        this.issueRefreshTokenUseCase = issueRefreshTokenUseCase;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        final var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        final var authenticationManager = new ProviderManager(provider);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/v1/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/cities/search").permitAll()
                        .requestMatchers("/error/**", "/v3/**", "/swagger-ui/**", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/v1/users/search").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/users/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/users/*/permissions").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/v1/users/*/active").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtService, objectMapper(), issueRefreshTokenUseCase, jwtProperties.refreshTokenExpirationDays()))
                .addFilterBefore(new JwtAuthorizationFilter(jwtService, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*", "http://localhost:5173/", "http://127.0.0.1:5173/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "OPTIONS", "DELETE"));
        configuration.setAllowedHeaders(List.of(
                "Authorization", "x-xsrf-token",
                "Access-Control-Allow-Headers", "Origin",
                "Accept", "X-Requested-With", "Content-Type",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers", "Auth-Id-Token"
        ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
