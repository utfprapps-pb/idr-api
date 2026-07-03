package br.gov.pr.idr.application.property_management.city;

import br.gov.pr.idr.application.property_management.city.update.UpdateCityCommand;
import br.gov.pr.idr.application.property_management.city.update.UpdateCityUseCase;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateCityUseCase")
class UpdateCityUseCaseTest {

    @Mock CityGateway cityGateway;
    @Mock RegionGateway regionGateway;
    @InjectMocks UpdateCityUseCase useCase;

    private final UUID cityId = UUID.randomUUID();
    private final UUID regionId = UUID.randomUUID();

    @Test
    @DisplayName("deve atualizar cidade com sucesso")
    void shouldUpdateCity() {
        final var city = City.create("Curitiba", State.PR, RegionID.from(regionId));
        when(cityGateway.findById(any())).thenReturn(Optional.of(city));
        when(regionGateway.existsById(any())).thenReturn(true);
        when(cityGateway.update(any())).thenReturn(city);

        final var output = useCase.execute(UpdateCityCommand.from(cityId, regionId, "Pinhais"));

        assertNotNull(output);
        verify(cityGateway).update(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando cidade não é encontrada")
    void shouldThrowWhenCityNotFound() {
        when(cityGateway.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotificationException.class,
                () -> useCase.execute(UpdateCityCommand.from(cityId, regionId, "Pinhais")));
        verify(cityGateway, never()).update(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando região não é encontrada")
    void shouldThrowWhenRegionNotFound() {
        final var city = City.create("Curitiba", State.PR, RegionID.from(regionId));
        when(cityGateway.findById(any())).thenReturn(Optional.of(city));
        when(regionGateway.existsById(any())).thenReturn(false);

        assertThrows(NotificationException.class,
                () -> useCase.execute(UpdateCityCommand.from(cityId, regionId, "Pinhais")));
    }
}
