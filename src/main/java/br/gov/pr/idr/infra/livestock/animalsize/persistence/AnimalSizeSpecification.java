package br.gov.pr.idr.infra.livestock.animalsize.persistence;

import br.gov.pr.idr.infra.shared.persistence.LikeSpecification;
import org.springframework.data.jpa.domain.Specification;

public class AnimalSizeSpecification {

    private AnimalSizeSpecification() {}

    public static Specification<AnimalSizeJPAEntity> withTerms(final String terms) {
        return LikeSpecification.withTerms(terms, "sizeName");
    }
}
