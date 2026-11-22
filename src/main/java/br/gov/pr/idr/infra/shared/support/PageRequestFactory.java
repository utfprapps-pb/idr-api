package br.gov.pr.idr.infra.shared.support;

import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public final class PageRequestFactory {

    private static final String ID = "id";

    private PageRequestFactory() {}

    public static PageRequest from(final SearchQuery query, final String defaultSort) {
        final var direction = "desc".equalsIgnoreCase(query.direction())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        final var sortField = query.sort() != null ? query.sort() : defaultSort;
        final var sort = Sort.by(direction, sortField);
        // Desempate pelo id: com campos repetidos (ex.: identificador do animal), o banco não garante a mesma
        // ordem entre páginas, e a paginação poderia repetir ou pular registros.
        return PageRequest.of(query.page(), query.perPage(),
                ID.equals(sortField) ? sort : sort.and(Sort.by(Sort.Direction.ASC, ID)));
    }

    public static PageRequest from(final SearchQuery query) {
        return from(query, "name");
    }

    public static PageRequest from(final SearchUserQuery query) {
        return from(query.query());
    }

    public static String terms(final String terms) {
        return terms != null && !terms.isBlank() ? terms.trim() : "";
    }

    /**
     * Lê o {@code terms} de uma busca como data ISO ({@code yyyy-MM-dd}). Vazio quando {@code terms} está em branco
     * ou não é uma data válida: a busca segue sem filtro de data.
     */
    public static Optional<LocalDate> dateTerm(final String terms) {
        final var value = terms(terms);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(value));
        } catch (final DateTimeParseException ex) {
            return Optional.empty();
        }
    }
}
