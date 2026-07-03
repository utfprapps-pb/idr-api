package br.gov.pr.idr.domain.property_management.city;

import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

public class City extends AggregateRoot<CityID> {

    private String name;
    private final State state;
    private RegionID regionId;

    protected City(final CityID id, final String name, final State state, final RegionID regionId) {
        super(id);
        this.name = name;
        this.state = state;
        this.regionId = regionId;
    }

    public static City with(final CityID id, final String name, final State state, final RegionID regionId){
        return new City(id, name, state, regionId);
    }

    public static City create(final String name, final State state, final RegionID regionId) {
        final var city = new City(CityID.unique(), name, state, regionId);
        city.selfValidate();
        return city;
    }

    public City update(final RegionID regionId, final String name) {
        this.regionId = regionId;
        this.name = name;
        selfValidate();
        return this;
    }


    @Override
    public void validate(ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("O nome da cidade é obrigatório"));
        }
        if (this.state == null) {
            handler.append(DomainError.from("O estado da cidade é obrigatório"));
        }
        if (this.regionId == null) {
            handler.append(DomainError.from("A região da cidade é obrigatória"));
        }
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public RegionID getRegionId() {
        return regionId;
    }
}
