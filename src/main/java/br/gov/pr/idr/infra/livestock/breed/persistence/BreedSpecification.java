package br.gov.pr.idr.infra.livestock.breed.persistence;

import br.gov.pr.idr.infra.shared.persistence.LikeSpecification;
import org.springframework.data.jpa.domain.Specification;

public class BreedSpecification {

    private BreedSpecification() {}

    public static Specification<BreedJPAEntity> withTerms(final String terms) {
        return LikeSpecification.withTerms(terms, "breedName");
    }
}
