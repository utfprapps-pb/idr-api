package br.gov.pr.idr.domain.property_management.city;

import java.util.Optional;

public interface CityGateway {

    boolean existsById(CityID id);

    City save(final City city);

    Optional<City> findById(final CityID cityId);

    City update(final City city);
}
