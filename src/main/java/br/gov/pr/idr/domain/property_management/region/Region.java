package br.gov.pr.idr.domain.property_management.region;

import br.gov.pr.idr.domain.shared.AggregateRoot;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

public class Region extends AggregateRoot<RegionID> {

    private String description;

    protected Region(final RegionID id, final String description) {
        super(id);
        this.description = description;
        selfValidate();
    }

    public static Region create(final String description) {
        return new Region(RegionID.unique(), description);
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
