package br.gov.pr.idr.application.property_management.region;

import br.gov.pr.idr.application.property_management.region.update.UpdateRegionCommand;
import br.gov.pr.idr.application.property_management.region.update.UpdateRegionUseCase;
import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
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
@DisplayName("UpdateRegionUseCase")
class UpdateRegionUseCaseTest {

    @Mock RegionGateway regionGateway;
    @InjectMocks UpdateRegionUseCase useCase;

    @Test
    @DisplayName("deve atualizar região com sucesso")
    void shouldUpdateRegion() {
        final var region = Region.create("Região Sul");
        when(regionGateway.findByID(any())).thenReturn(Optional.of(region));
        when(regionGateway.existsByDescription("Região Sul Atualizada")).thenReturn(false);
        when(regionGateway.update(any())).thenReturn(region);

        final var output = useCase.execute(new UpdateRegionCommand(UUID.randomUUID(), "Região Sul Atualizada"));

        assertNotNull(output);
        verify(regionGateway).update(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando região não é encontrada")
    void shouldThrowWhenRegionNotFound() {
        when(regionGateway.findByID(any())).thenReturn(Optional.empty());

        assertThrows(NotificationException.class,
                () -> useCase.execute(new UpdateRegionCommand(UUID.randomUUID(), "Qualquer")));
        verify(regionGateway, never()).update(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando nova descrição já existe")
    void shouldThrowWhenDescriptionAlreadyExists() {
        final var region = Region.create("Região Norte");
        when(regionGateway.findByID(any())).thenReturn(Optional.of(region));
        when(regionGateway.existsByDescription("Região Norte")).thenReturn(true);

        assertThrows(NotificationException.class,
                () -> useCase.execute(new UpdateRegionCommand(UUID.randomUUID(), "Região Norte")));
    }
}
