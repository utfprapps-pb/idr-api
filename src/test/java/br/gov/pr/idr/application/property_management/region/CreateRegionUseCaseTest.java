package br.gov.pr.idr.application.property_management.region;

import br.gov.pr.idr.application.property_management.region.create.CreateRegionCommand;
import br.gov.pr.idr.application.property_management.region.create.CreateRegionUseCase;
import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateRegionUseCase")
class CreateRegionUseCaseTest {

    @Mock RegionGateway regionGateway;
    @InjectMocks CreateRegionUseCase useCase;

    @Test
    @DisplayName("deve criar região com sucesso")
    void shouldCreateRegion() {
        final var region = Region.create("Região Norte");
        when(regionGateway.existsByDescription("Região Norte")).thenReturn(false);
        when(regionGateway.save(any())).thenReturn(region);

        final var output = useCase.execute(new CreateRegionCommand("Região Norte"));

        assertNotNull(output);
        assertEquals(region.getId().id(), output.id());
        verify(regionGateway).save(any());
    }

    @Test
    @DisplayName("deve lançar exceção se descrição já existe")
    void shouldThrowWhenDescriptionAlreadyExists() {
        when(regionGateway.existsByDescription("Região Norte")).thenReturn(true);

        assertThrows(NotificationException.class,
                () -> useCase.execute(new CreateRegionCommand("Região Norte")));
        verify(regionGateway, never()).save(any());
    }
}
