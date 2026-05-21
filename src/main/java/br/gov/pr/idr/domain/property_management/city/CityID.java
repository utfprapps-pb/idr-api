package br.gov.pr.idr.domain.property_management.city;

import br.gov.pr.idr.domain.shared.Identifier;

public record CityID(Long id) implements Identifier {

    public static CityID unique() {
        return from(null);
    }
    public static CityID from(final Long id) {
        return new CityID(id);
    }
}
