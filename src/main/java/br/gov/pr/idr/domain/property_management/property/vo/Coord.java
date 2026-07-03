package br.gov.pr.idr.domain.property_management.property.vo;

import br.gov.pr.idr.domain.shared.tactical.ValueObject;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;

import java.math.BigDecimal;

@ValueObject
public record Coord(BigDecimal latitude, BigDecimal longitude) {

    public Coord {
        if (latitude == null || longitude == null) {
            throw DomainException.from("Latitude and longitude cannot be null");
        }


    }

    public static Coord from(BigDecimal latitude, BigDecimal longitude) {
        return new Coord(latitude, longitude);
    }
}
