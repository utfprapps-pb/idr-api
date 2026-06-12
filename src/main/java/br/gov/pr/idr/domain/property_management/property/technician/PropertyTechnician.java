package br.gov.pr.idr.domain.property_management.property.technician;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaboratorID;
import br.gov.pr.idr.domain.shared.Entity;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

public class PropertyTechnician extends Entity<PropertyTechnicianID> {

    private UserID userID;
    private PropertyID propertyID;

    protected PropertyTechnician(final PropertyTechnicianID id,
                                 final UserID userID,
                                 final PropertyID propertyID
    ) {
        super(id);
       this.userID = userID;
       this.propertyID = propertyID;
        selfValidate();
    }


    @Override
    public void validate(ValidationHandler handler) {

    }
}
