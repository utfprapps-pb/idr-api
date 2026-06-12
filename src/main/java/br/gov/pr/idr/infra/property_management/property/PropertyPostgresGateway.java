package br.gov.pr.idr.infra.property_management.property;

import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPAEntity;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertyPostgresGateway implements PropertyGateway {

    private final PropertyJPARepository repository;

    @Override
    public Property save(Property property) {
        return repository.save(PropertyJPAEntity.fromDomain(property))
                .toDomain();
    }
}
