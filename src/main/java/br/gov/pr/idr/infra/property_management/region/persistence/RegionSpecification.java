package br.gov.pr.idr.infra.property_management.region.persistence;

import org.springframework.data.jpa.domain.Specification;

public class RegionSpecification {

    private RegionSpecification() {}

    public static Specification<RegionJPAEntity> withTerms(final String terms) {
        return (root, query, cb) -> {
            if (terms == null || terms.isBlank()) return cb.conjunction();
            final var pattern = "%" + terms.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("description")), pattern);
        };
    }
}
