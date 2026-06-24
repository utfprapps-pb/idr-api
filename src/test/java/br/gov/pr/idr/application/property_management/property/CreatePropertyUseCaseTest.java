package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyCommand;
import br.gov.pr.idr.application.property_management.property.create.CreatePropertyUseCase;
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

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyCommand.CollaboratorData;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePropertyUseCase")
class CreatePropertyUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @Mock CityGateway cityGateway;
    @Mock ProducerGateway producerGateway;
    @InjectMocks CreatePropertyUseCase useCase;

    private final UUID cityId = UUID.randomUUID();
    private final UUID producerId = UUID.randomUUID();

    private CreatePropertyCommand validCommand() {
        return CreatePropertyCommand.from(
                "Fazenda Boa Vista",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, List.of(), List.of()
        );
    }

    private Property stubProperty() {
        return Property.with(PropertyID.unique(), "Fazenda Boa Vista",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                br.gov.pr.idr.domain.property_management.producer.ProducerID.from(producerId),
                br.gov.pr.idr.domain.property_management.city.CityID.from(cityId),
                List.of(), List.of());
    }

    @Test
    @DisplayName("deve criar propriedade com sucesso")
    void shouldCreateProperty() {
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(stubProperty());

        final var output = useCase.execute(validCommand());

        assertNotNull(output);
        verify(propertyGateway).save(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando cidade não existe")
    void shouldThrowWhenCityNotFound() {
        when(cityGateway.existsById(any())).thenReturn(false);
        final var cmd = validCommand();

        assertThrows(NotFoundException.class, () -> useCase.execute(cmd));
        verify(propertyGateway, never()).save(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando produtor não existe")
    void shouldThrowWhenProducerNotFound() {
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(false);
        final var cmd = validCommand();

        assertThrows(NotFoundException.class, () -> useCase.execute(cmd));
        verify(propertyGateway, never()).save(any());
    }

    @Test
    @DisplayName("deve criar propriedade com technicianIds e collaborators nulos convertendo para listas vazias")
    void shouldCreatePropertyWithNullListsConvertedToEmpty() {
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(stubProperty());

        final var cmd = CreatePropertyCommand.from(
                "Fazenda Boa Vista",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, null, null
        );
        final var output = useCase.execute(cmd);

        assertNotNull(output);
    }

    @Test
    @DisplayName("deve criar propriedade com colaboradores informados")
    void shouldCreatePropertyWithCollaborators() {
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(stubProperty());

        final var collaborator = new CollaboratorData("Carlos", "8");
        final var cmd = CreatePropertyCommand.from(
                "Fazenda Boa Vista",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, List.of(), List.of(collaborator)
        );
        final var output = useCase.execute(cmd);

        assertNotNull(output);
    }
}
