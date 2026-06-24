package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyCommand;
import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyUseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdatePropertyUseCase")
class UpdatePropertyUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @Mock CityGateway cityGateway;
    @Mock ProducerGateway producerGateway;
    @InjectMocks UpdatePropertyUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID cityId = UUID.randomUUID();
    private final UUID producerId = UUID.randomUUID();

    private UpdatePropertyCommand validCommand() {
        return UpdatePropertyCommand.from(
                propertyId, "Fazenda Atualizada",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, null, null
        );
    }

    private Property stubProperty() {
        return Property.with(
                PropertyID.from(propertyId), "Fazenda",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                br.gov.pr.idr.domain.property_management.producer.ProducerID.from(producerId),
                br.gov.pr.idr.domain.property_management.city.CityID.from(cityId),
                List.of(), List.of()
        );
    }

    @Test
    @DisplayName("deve atualizar propriedade com sucesso")
    void shouldUpdateProperty() {
        final var property = stubProperty();
        when(propertyGateway.findById(any())).thenReturn(Optional.of(property));
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(true);
        when(propertyGateway.update(any())).thenReturn(property);

        final var output = useCase.execute(validCommand());

        assertNotNull(output);
        verify(propertyGateway).update(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando propriedade não existe")
    void shouldThrowWhenPropertyNotFound() {
        when(propertyGateway.findById(any())).thenReturn(Optional.empty());
        final var cmd = validCommand();

        assertThrows(NotFoundException.class, () -> useCase.execute(cmd));
        verify(propertyGateway, never()).update(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando cidade não existe")
    void shouldThrowWhenCityNotFound() {
        when(propertyGateway.findById(any())).thenReturn(Optional.of(stubProperty()));
        when(cityGateway.existsById(any())).thenReturn(false);
        final var cmd = validCommand();

        assertThrows(NotFoundException.class, () -> useCase.execute(cmd));
        verify(propertyGateway, never()).update(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando produtor não existe")
    void shouldThrowWhenProducerNotFound() {
        when(propertyGateway.findById(any())).thenReturn(Optional.of(stubProperty()));
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(false);
        final var cmd = validCommand();

        assertThrows(NotFoundException.class, () -> useCase.execute(cmd));
        verify(propertyGateway, never()).update(any());
    }

    @Test
    @DisplayName("deve converter technicianIds e collaborators nulos em listas vazias")
    void shouldConvertNullListsToEmpty() {
        final var cmd = UpdatePropertyCommand.from(
                propertyId, "Fazenda", new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                producerId, cityId, null, null);

        assertTrue(cmd.technicianIds().isEmpty());
        assertTrue(cmd.collaborators().isEmpty());
    }

    @Test
    @DisplayName("deve preservar technicianIds e collaborators quando não nulos")
    void shouldPreserveNonNullLists() {
        final var techId = UUID.randomUUID();
        final var collab = new UpdatePropertyCommand.CollaboratorData("Ana", "8h");
        final var technicianIds = List.of(techId);
        final var collaborators = List.of(collab);
        final var cmd = UpdatePropertyCommand.from(
                propertyId, "Fazenda", new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                producerId, cityId, technicianIds, collaborators);

        assertEquals(1, cmd.technicianIds().size());
        assertEquals(techId, cmd.technicianIds().getFirst());
        assertEquals(1, cmd.collaborators().size());
        assertEquals("Ana", cmd.collaborators().getFirst().name());
    }
}
