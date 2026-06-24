package br.gov.pr.idr.application.property_management.city;

import br.gov.pr.idr.application.property_management.city.create.CreateCityCommand;
import br.gov.pr.idr.application.property_management.city.create.CreateCityUseCase;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCityUseCase")
class CreateCityUseCaseTest {

    @Mock CityGateway cityGateway;
    @Mock RegionGateway regionGateway;
    @InjectMocks CreateCityUseCase useCase;

    private final UUID regionId = UUID.randomUUID();

    @Test
    @DisplayName("deve criar cidade com sucesso quando região existe")
    void shouldCreateCityWhenRegionExists() {
        final var city = City.create("Curitiba", State.PR, RegionID.from(regionId));
        when(regionGateway.existsById(any())).thenReturn(true);
        when(cityGateway.save(any())).thenReturn(city);

        final var output = useCase.execute(CreateCityCommand.from("Curitiba", State.PR, regionId));

        assertNotNull(output);
        assertEquals(city.getId().id(), output.id());
        verify(cityGateway).save(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando região não existe")
    void shouldThrowWhenRegionNotFound() {
        when(regionGateway.existsById(any())).thenReturn(false);

        assertThrows(NotificationException.class,
                () -> useCase.execute(CreateCityCommand.from("Curitiba", State.PR, regionId)));
        verify(cityGateway, never()).save(any());
    }
}
