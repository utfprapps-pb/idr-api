package br.gov.pr.idr.domain.property_management.region;

import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

public class Region extends AggregateRoot<RegionID> {

    private String description;

    protected Region(final RegionID id, final String description) {
        super(id);
        this.description = description;
    }

    public static Region create(final String description) {
        final var region = new Region(RegionID.unique(), description);
        region.selfValidate();
        return region;
    }

    public static Region with(final RegionID id, final String description) {
        return new Region(id, description);
    }

    public Region update(final String description) {
        this.description = description;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.description == null || this.description.isBlank()) {
            handler.append(DomainError.from("Descrição da região não pode ser vazio"));
        }
    }

    public String getDescription() {
        return description;
    }
}
