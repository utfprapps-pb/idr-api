package br.gov.pr.idr.domain.property_management.property.collaborator;

import br.gov.pr.idr.domain.shared.tactical.Entity;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

public class PropertyCollaborator extends Entity<PropertyCollaboratorID> {

    private final String name;
    private final String hoursPerDay;

    protected PropertyCollaborator(final PropertyCollaboratorID id,
                                   final String name,
                                   final String hoursPerDay
    ) {
        super(id);
        this.name = name;
        this.hoursPerDay = hoursPerDay;
    }

    public static PropertyCollaborator create(final String name,
                                              final String hoursPerDay
    ) {
        final var collaborator = new PropertyCollaborator(PropertyCollaboratorID.unique(), name, hoursPerDay);
        collaborator.selfValidate();
        return collaborator;
    }

    public static PropertyCollaborator with(final PropertyCollaboratorID id,
                                            final String name,
                                            final String hoursPerDay
    ) {
        return new PropertyCollaborator(id, name, hoursPerDay);
    }


    @Override
    public void validate(ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("Nome do trabalhador é obrigatório"));
        }
        if (this.hoursPerDay == null || this.hoursPerDay.isBlank()) {
            handler.append(DomainError.from("Horas de trabalho é obrigatório"));
        }
    }

    public String getName() {
        return name;
    }

    public String getHoursPerDay() {
        return hoursPerDay;
    }
}
