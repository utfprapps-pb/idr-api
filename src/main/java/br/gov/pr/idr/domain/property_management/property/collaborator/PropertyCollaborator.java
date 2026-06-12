package br.gov.pr.idr.domain.property_management.property.collaborator;

import br.gov.pr.idr.domain.shared.Entity;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

public class PropertyCollaborator extends Entity<PropertyCollaboratorID> {

    private String name;
    private String hoursPerDay;

    protected PropertyCollaborator(final PropertyCollaboratorID id,
                                   final String name,
                                   final String hoursPerDay
    ) {
        super(id);
        this.name = name;
        this.hoursPerDay = hoursPerDay;
        selfValidate();
    }

    public static PropertyCollaborator create(final String name,
                                              final String hoursPerDay
    ) {
        return new PropertyCollaborator(
                PropertyCollaboratorID.unique(),
                name,
                hoursPerDay
        );
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
}
