package br.gov.pr.idr.infra.shared.persistence;

import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class LikeSpecification {

    private static final char ESCAPE = '\\';

    private LikeSpecification() {}

    /** "Contém" sem diferenciar maiúsculas em {@code field}; {@code %} e {@code _} nos termos valem literalmente. */
    public static <T> Specification<T> withTerms(final String terms, final String field) {
        return (root, query, cb) -> {
            if (terms == null || terms.isBlank()) return cb.conjunction();
            final var pattern = "%" + escape(terms.toLowerCase(Locale.ROOT)) + "%";
            return cb.like(cb.lower(root.get(field)), pattern, ESCAPE);
        };
    }

    private static String escape(final String terms) {
        return terms.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
