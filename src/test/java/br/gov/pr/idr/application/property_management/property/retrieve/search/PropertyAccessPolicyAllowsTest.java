package br.gov.pr.idr.application.property_management.property.retrieve.search;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PropertyAccessPolicy#allows")
class PropertyAccessPolicyAllowsTest {

    @Mock
    private CityGateway cityGateway;

    private Property someProperty() {
        return propertyWithCity(CityID.unique());
    }

    private Property propertyWithTechnicians(final List<UserID> technicianIds) {
        return Property.with(PropertyID.unique(), "Fazenda", null, null, null, null, null, null, null,
                ProducerID.unique(), CityID.unique(), technicianIds, List.of(), null, null);
    }

    private Property propertyWithCity(final CityID cityId) {
        return Property.with(PropertyID.unique(), "Fazenda", null, null, null, null, null, null, null,
                ProducerID.unique(), cityId, List.of(), List.of(), null, null);
    }

    @Test
    @DisplayName("unrestricted sempre permite")
    void unrestrictedAlwaysAllows() {
        final var scope = PropertySearchScope.unrestricted();
        assertTrue(PropertyAccessPolicy.allows(scope, someProperty(), cityGateway));
    }

    @Test
    @DisplayName("byTechnician permite quando o técnico está listado")
    void byTechnicianAllowsWhenTechnicianIsListed() {
        final var technicianId = UserID.unique();
        final var scope = PropertySearchScope.byTechnician(technicianId);
        final var property = propertyWithTechnicians(List.of(technicianId));
        assertTrue(PropertyAccessPolicy.allows(scope, property, cityGateway));
    }

    @Test
    @DisplayName("byTechnician nega quando o técnico não está listado")
    void byTechnicianDeniesWhenTechnicianIsNotListed() {
        final var scope = PropertySearchScope.byTechnician(UserID.unique());
        final var property = propertyWithTechnicians(List.of(UserID.unique()));
        assertFalse(PropertyAccessPolicy.allows(scope, property, cityGateway));
    }

    @Test
    @DisplayName("byLocation permite quando a cidade da propriedade está no escopo")
    void byLocationAllowsWhenPropertyCityIsInScope() {
        final var cityId = CityID.unique();
        final var scope = PropertySearchScope.byLocation(Set.of(), Set.of(cityId));
        final var property = propertyWithCity(cityId);
        assertTrue(PropertyAccessPolicy.allows(scope, property, cityGateway));
    }

    @Test
    @DisplayName("byLocation permite quando a região da cidade da propriedade está no escopo")
    void byLocationAllowsWhenPropertyCityRegionIsInScope() {
        final var regionId = RegionID.unique();
        final var cityId = CityID.unique();
        final var property = propertyWithCity(cityId);
        when(cityGateway.findById(cityId)).thenReturn(java.util.Optional.of(City.with(cityId, "C", State.PR, regionId)));
        final var scope = PropertySearchScope.byLocation(Set.of(regionId), Set.of());
        assertTrue(PropertyAccessPolicy.allows(scope, property, cityGateway));
    }

    @Test
    @DisplayName("byLocation nega quando nem cidade nem região correspondem")
    void byLocationDeniesWhenNeitherCityNorRegionMatch() {
        final var property = propertyWithCity(CityID.unique());
        when(cityGateway.findById(any())).thenReturn(java.util.Optional.of(City.with(CityID.unique(), "C", State.PR, RegionID.unique())));
        final var scope = PropertySearchScope.byLocation(Set.of(RegionID.unique()), Set.of(CityID.unique()));
        assertFalse(PropertyAccessPolicy.allows(scope, property, cityGateway));
    }
}
