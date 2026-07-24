package br.gov.pr.idr.domain.property_management.property.vo;

import br.gov.pr.idr.domain.shared.tactical.ValueObject;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;

import java.math.BigDecimal;

@ValueObject
public record Coord(BigDecimal latitude, BigDecimal longitude) {

    public Coord {
        if (latitude == null || longitude == null) {
            throw DomainException.from("Latitude e Longitude não podem ser nulos");
        }
        if (latitude.compareTo(new BigDecimal("-90")) < 0 || latitude.compareTo(new BigDecimal("90")) > 0) {
            throw DomainException.from("Latitude não pode ser inferior a -90 ou superior a 90");
        }
        if (longitude.compareTo(new BigDecimal("-180")) < 0 || longitude.compareTo(new BigDecimal("180")) > 0) {
            throw DomainException.from("Longitude não pode ser inferior a -180 ou superior a 180");
        }
    }

    public static Coord from(BigDecimal latitude, BigDecimal longitude) {
        return new Coord(latitude, longitude);
    }
}
