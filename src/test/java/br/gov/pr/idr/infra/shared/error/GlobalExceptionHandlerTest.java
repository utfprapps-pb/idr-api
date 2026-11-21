package br.gov.pr.idr.infra.shared.error;

import br.gov.pr.idr.domain.iam.refresh_token.exceptions.InvalidRefreshTokenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.core.TypeInformation;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("GlobalExceptionHandler: erros no formato Problem Details (RFC 9457)")
class GlobalExceptionHandlerTest {

    private static final String URN = "urn:idr:problem:";

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        final var messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        final var handler = new GlobalExceptionHandler();
        handler.setMessageSource(messageSource);
        mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
                .setControllerAdvice(handler)
                .build();
    }

    private static ResultActions expectProblem(final ResultActions result, final int status, final String type,
                                               final String title, final String instance) throws Exception {
        return result
                .andExpect(status().is(status))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value(type))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.status").value(status))
                .andExpect(jsonPath("$.instance").value(instance))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.timestamp").doesNotExist());
    }

    @Test
    @DisplayName("NotificationException: 400 validacao com errors por campo")
    void notificationExceptionIsValidationWithErrors() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/notification")), 400, URN + "validacao", "Erro de validação",
                "/throw/notification")
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("Nome é obrigatório"));
    }

    @Test
    @DisplayName("Bean Validation: 400 validacao com errors por campo")
    void beanValidationIsValidationWithErrors() throws Exception {
        expectProblem(mockMvc.perform(post("/throw/valid").contentType(MediaType.APPLICATION_JSON).content("{}")),
                400, URN + "validacao", "Erro de validação", "/throw/valid")
                .andExpect(jsonPath("$.detail").value("Erro de validação"))
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("Nome é obrigatório"));
    }

    @Test
    @DisplayName("corpo ausente: 400 validacao sem errors artificial")
    void unreadableBodyIsValidation() throws Exception {
        expectProblem(mockMvc.perform(post("/throw/valid").contentType(MediaType.APPLICATION_JSON)),
                400, URN + "validacao", "Erro de validação", "/throw/valid")
                .andExpect(jsonPath("$.detail").value("Corpo da requisição ausente ou inválido"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    @DisplayName("campo de ordenação desconhecido: 400 validacao")
    void unknownSortIsValidation() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/sort")), 400, URN + "validacao", "Erro de validação", "/throw/sort")
                .andExpect(jsonPath("$.detail").value(startsWith("Campo de ordenação inválido: ")));
    }

    @Test
    @DisplayName("NotFoundException: 404 nao-encontrado")
    void notFoundExceptionIs404() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/not-found")), 404, URN + "nao-encontrado",
                "Recurso não encontrado", "/throw/not-found")
                .andExpect(jsonPath("$.detail").value("Animal não encontrado"));
    }

    @Test
    @DisplayName("UnprocessableEntityException: 422 regra-de-negocio")
    void unprocessableIs422() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/unprocessable")), 422, URN + "regra-de-negocio",
                "Regra de negócio violada", "/throw/unprocessable")
                .andExpect(jsonPath("$.detail").value("Entidade não pôde ser processada"));
    }

    @Test
    @DisplayName("ForbiddenException: 403 acesso-negado")
    void forbiddenExceptionIs403() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/forbidden")), 403, URN + "acesso-negado", "Acesso negado",
                "/throw/forbidden")
                .andExpect(jsonPath("$.detail").value("Usuário não tem acesso à propriedade"));
    }

    @Test
    @DisplayName("DomainException: 400 validacao sem errors quando a lista é vazia")
    void domainExceptionIsValidation() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/domain")), 400, URN + "validacao", "Erro de validação",
                "/throw/domain")
                .andExpect(jsonPath("$.detail").value("CPF já cadastrado"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    @DisplayName("UUID inválido no path: 400, não 500")
    void invalidUuidInPathIsNot500() throws Exception {
        mockMvc.perform(get("/throw/uuid/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("método não suportado: 405 sem categoria própria (about:blank)")
    void unsupportedMethodIs405() throws Exception {
        mockMvc.perform(delete("/throw/not-found"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                // Spring 7 omite o type about:blank; pela RFC 9457 §3.1.1, ausência equivale a about:blank.
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    @DisplayName("Exception genérica: 500 erro-interno sem vazar a mensagem interna")
    void genericExceptionDoesNotLeakMessage() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/generic")), 500, URN + "erro-interno", "Erro interno",
                "/throw/generic")
                .andExpect(jsonPath("$.detail").value("Erro inesperado, tente novamente mais tarde"))
                .andExpect(content().string(not(containsString("SELECT"))));
    }

    @Test
    @DisplayName("Accept do axios (application/json): continua problem+json")
    void problemDetailWithJsonAcceptHeader() throws Exception {
        mockMvc.perform(get("/throw/not-found").accept("application/json, text/plain, */*"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(URN + "nao-encontrado"))
                .andExpect(jsonPath("$.detail").value("Animal não encontrado"));
    }

    @Test
    @DisplayName("UUID inválido no path: 400 validacao com texto em português")
    void invalidUuidInPathHasPortugueseDetail() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/uuid/abc")), 400, URN + "validacao", "Erro de validação",
                "/throw/uuid/abc")
                .andExpect(jsonPath("$.detail").value("Valor inválido para o parâmetro 'id': 'abc'"));
    }

    @Test
    @DisplayName("parâmetro obrigatório ausente: 400 validacao com texto em português")
    void missingParamHasPortugueseDetail() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/param")), 400, URN + "validacao", "Erro de validação",
                "/throw/param")
                .andExpect(jsonPath("$.detail").value("Parâmetro obrigatório 'page' ausente"));
    }

    @Test
    @DisplayName("método não suportado: título e texto em português")
    void unsupportedMethodHasPortugueseTitle() throws Exception {
        mockMvc.perform(delete("/throw/not-found"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.title").value("Método não permitido"))
                .andExpect(jsonPath("$.detail").value("Método DELETE não suportado"));
    }

    @Test
    @DisplayName("Content-Type não suportado: 415 com título em português")
    void unsupportedMediaTypeHasPortugueseDetail() throws Exception {
        mockMvc.perform(post("/throw/valid").contentType(MediaType.TEXT_PLAIN).content("x"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.title").value("Tipo de conteúdo não suportado"))
                .andExpect(jsonPath("$.detail").value("Tipo de conteúdo text/plain não suportado"));
    }

    @Test
    @DisplayName("refresh token inválido: 400 com type próprio")
    void invalidRefreshTokenIs400WithOwnType() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/refresh")), 400, URN + "refresh-token-invalido", "Sessão expirada",
                "/throw/refresh")
                .andExpect(jsonPath("$.detail").value("Refresh token inválido ou expirado."));
    }

    @Test
    @DisplayName("senha errada no login: 401 credenciais-invalidas")
    void badCredentialsIs401() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/bad-credentials")), 401, URN + "credenciais-invalidas",
                "Credenciais inválidas", "/throw/bad-credentials")
                .andExpect(jsonPath("$.detail").value("Usuário ou senha inválidos"));
    }

    @Test
    @DisplayName("usuário inativo no login: 403 cadastro-inativo")
    void disabledIs403() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/disabled")), 403, URN + "cadastro-inativo", "Cadastro inativo",
                "/throw/disabled")
                .andExpect(jsonPath("$.detail")
                        .value("Seu cadastro está inativo, solicite a um administrador para ativá-lo"));
    }

    @Test
    @DisplayName("não autenticado: 401 nao-autenticado sem detail")
    void insufficientAuthenticationIs401() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/unauthenticated")), 401, URN + "nao-autenticado",
                "Não autenticado", "/throw/unauthenticated")
                .andExpect(jsonPath("$.detail").doesNotExist());
    }

    @Test
    @DisplayName("sem autoridade: 403 acesso-negado sem detail")
    void accessDeniedIs403() throws Exception {
        expectProblem(mockMvc.perform(get("/throw/access-denied")), 403, URN + "acesso-negado", "Acesso negado",
                "/throw/access-denied")
                .andExpect(jsonPath("$.detail").doesNotExist());
    }

    @RestController
    @RequestMapping("/throw")
    static class ThrowingController {

        record Body(@NotBlank(message = "Nome é obrigatório") String name) {
        }

        @GetMapping("/notification")
        void notification() {
            final var validation = NotificationValidation.create();
            validation.append(DomainError.from("name", "Nome é obrigatório"));
            throw new NotificationException(validation);
        }

        @PostMapping("/valid")
        void valid(@Valid @RequestBody final Body body) {
        }

        @GetMapping("/sort")
        void sort() {
            throw new PropertyReferenceException("foo", TypeInformation.of(Body.class), List.of());
        }

        @GetMapping("/not-found")
        void notFound() {
            throw NotFoundException.with("Animal não encontrado");
        }

        @GetMapping("/unprocessable")
        void unprocessable() {
            throw new UnprocessableEntityException("Entidade não pôde ser processada");
        }

        @GetMapping("/forbidden")
        void forbidden() {
            throw ForbiddenException.with("Usuário não tem acesso à propriedade");
        }

        @GetMapping("/domain")
        void domain() {
            throw DomainException.from("CPF já cadastrado");
        }

        @GetMapping("/uuid/{id}")
        void uuid(@PathVariable final UUID id) {
        }

        @GetMapping("/param")
        void param(@RequestParam final int page) {
        }

        @GetMapping("/refresh")
        void refresh() {
            throw InvalidRefreshTokenException.with("Refresh token inválido ou expirado.");
        }

        @GetMapping("/bad-credentials")
        void badCredentials() {
            throw new BadCredentialsException("Bad credentials");
        }

        @GetMapping("/disabled")
        void disabled() {
            throw new DisabledException("User is disabled");
        }

        @GetMapping("/unauthenticated")
        void unauthenticated() {
            throw new InsufficientAuthenticationException("Full authentication is required");
        }

        @GetMapping("/access-denied")
        void accessDenied() {
            throw new AccessDeniedException("Access Denied");
        }

        @GetMapping("/generic")
        void generic() {
            throw new IllegalStateException("wrapper", new RuntimeException("SELECT * FROM users"));
        }
    }
}
