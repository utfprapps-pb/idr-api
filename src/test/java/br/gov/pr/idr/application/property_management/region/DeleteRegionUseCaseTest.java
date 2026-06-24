package br.gov.pr.idr.application.property_management.region;

import br.gov.pr.idr.application.property_management.region.delete.DeleteRegionUseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteRegionUseCase")
class DeleteRegionUseCaseTest {

    @Mock RegionGateway regionGateway;
    @Mock CityGateway cityGateway;
    @InjectMocks DeleteRegionUseCase useCase;

    @Test
    @DisplayName("deve deletar região sem cidades vinculadas")
    void shouldDeleteRegionWithoutCities() {
        when(regionGateway.existsById(any())).thenReturn(true);
        when(cityGateway.existsByRegionId(any())).thenReturn(false);

        useCase.execute(UUID.randomUUID());

        verify(regionGateway).deleteById(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando região não existe")
    void shouldThrowWhenRegionNotFound() {
        when(regionGateway.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
        verify(regionGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("deve lançar NotificationException quando região possui cidades vinculadas")
    void shouldThrowWhenRegionHasCities() {
        when(regionGateway.existsById(any())).thenReturn(true);
        when(cityGateway.existsByRegionId(any())).thenReturn(true);

        assertThrows(NotificationException.class, () -> useCase.execute(UUID.randomUUID()));
        verify(regionGateway, never()).deleteById(any());
    }
}
