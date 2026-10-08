package br.gov.pr.idr.infra.livestock.breed.persistence;

import org.springframework.data.jpa.domain.Specification;

public class BreedSpecification {

    private BreedSpecification() {}

    public static Specification<BreedJPAEntity> withTerms(final String terms) {
        return (root, query, cb) -> {
            if (terms == null || terms.isBlank()) return cb.conjunction();
            final var pattern = "%" + terms.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("breedName")), pattern);
        };
    }
}
