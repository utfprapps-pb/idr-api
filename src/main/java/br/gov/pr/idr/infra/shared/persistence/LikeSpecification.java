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
            return cb.like(cb.lower(root.get(field)), containsPattern(terms), ESCAPE);
        };
    }

    /**
     * Padrão de "contém" em minúsculas, com {@code %}, {@code _} e {@code \} escapados, para consultas JPQL
     * que usam {@code LOWER(campo) LIKE :pattern ESCAPE '\'}. Termos vazios casam com tudo.
     */
    public static String containsPattern(final String terms) {
        return terms == null ? "%" : "%" + escape(terms.toLowerCase(Locale.ROOT)) + "%";
    }

    private static String escape(final String terms) {
        return terms.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
