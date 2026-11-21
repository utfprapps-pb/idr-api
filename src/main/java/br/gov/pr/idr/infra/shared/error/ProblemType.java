package br.gov.pr.idr.infra.shared.error;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import java.net.URI;

/**
 * Categorias de erro da API no formato Problem Details (RFC 9457): cada uma tem um {@code type} estável (URN) e
 * um {@code title} fixo; o {@code detail} explica a ocorrência.
 */
public enum ProblemType {

    VALIDACAO("validacao", "Erro de validação"),
    REFRESH_TOKEN_INVALIDO("refresh-token-invalido", "Sessão expirada"),
    NAO_ENCONTRADO("nao-encontrado", "Recurso não encontrado"),
    REGRA_DE_NEGOCIO("regra-de-negocio", "Regra de negócio violada"),
    ACESSO_NEGADO("acesso-negado", "Acesso negado"),
    NAO_AUTENTICADO("nao-autenticado", "Não autenticado"),
    CREDENCIAIS_INVALIDAS("credenciais-invalidas", "Credenciais inválidas"),
    CADASTRO_INATIVO("cadastro-inativo", "Cadastro inativo"),
    ERRO_INTERNO("erro-interno", "Erro interno");

    private static final String URN_PREFIX = "urn:idr:problem:";

    private final URI type;
    private final String title;

    ProblemType(final String suffix, final String title) {
        this.type = URI.create(URN_PREFIX + suffix);
        this.title = title;
    }

    public URI type() {
        return type;
    }

    public String title() {
        return title;
    }

    public ProblemDetail toProblemDetail(final HttpStatusCode status, final @Nullable String detail) {
        final var problem = ProblemDetail.forStatus(status);
        problem.setType(type);
        problem.setTitle(title);
        if (detail != null && !detail.isBlank()) {
            problem.setDetail(detail);
        }
        return problem;
    }
}
