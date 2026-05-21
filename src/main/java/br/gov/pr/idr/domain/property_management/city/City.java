package br.gov.pr.idr.domain.property_management.city;

import br.gov.pr.idr.domain.shared.Entity;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

public class City extends Entity<CityID> {

    protected City(CityID id) {
        super(id);
    }

    @Override
    public void validate(ValidationHandler handler) {

    }
}
