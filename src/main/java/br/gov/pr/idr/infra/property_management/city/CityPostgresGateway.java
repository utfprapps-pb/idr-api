package br.gov.pr.idr.infra.property_management.city;

import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CityPostgresGateway implements CityGateway {

    private final CityJPARepository repository;

    @Override
    public boolean existsById(final CityID id) {
        return repository.existsById(id.id());
    }
}
