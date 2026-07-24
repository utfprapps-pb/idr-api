package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyCommand;
import br.gov.pr.idr.application.property_management.property.create.CreatePropertyUseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyCommand.CollaboratorData;
import br.gov.pr.idr.application.property_management.property.create.CreatePropertyCommand.AttachmentData;

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
    @Mock StorageGateway storageGateway;
    @InjectMocks CreatePropertyUseCase useCase;

    private final UUID cityId = UUID.randomUUID();
    private final UUID producerId = UUID.randomUUID();

    private CreatePropertyCommand validCommand() {
        return CreatePropertyCommand.from(
                "Fazenda Boa Vista",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, List.of(), List.of(), List.of()
        );
    }

    private Property stubProperty() {
        return Property.with(PropertyID.unique(), "Fazenda Boa Vista",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                br.gov.pr.idr.domain.property_management.producer.ProducerID.from(producerId),
                br.gov.pr.idr.domain.property_management.city.CityID.from(cityId),
                List.of(), List.of(), null, null);
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
                producerId, cityId, null, null, null
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
                producerId, cityId, List.of(), List.of(collaborator), List.of()
        );
        final var output = useCase.execute(cmd);

        assertNotNull(output);
    }

    @Test
    @DisplayName("deve fazer upload de anexos válidos e associá-los à propriedade antes de salvar")
    void shouldUploadValidAttachments() {
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(stubProperty());

        final var attachment = new AttachmentData("contrato.pdf", "application/pdf", new byte[]{1, 2, 3});
        final var cmd = CreatePropertyCommand.from(
                "Fazenda Boa Vista",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, List.of(), List.of(), List.of(attachment)
        );

        useCase.execute(cmd);

        final var captor = ArgumentCaptor.forClass(Property.class);
        verify(propertyGateway).save(captor.capture());
        assertEquals(1, captor.getValue().getAttachments().size());
        verify(storageGateway).store(any(), eq("application/pdf"), any());
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException para tipo de arquivo não suportado, sem fazer upload")
    void shouldThrowWhenAttachmentContentTypeNotAllowed() {
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(true);

        final var attachment = new AttachmentData("virus.exe", "application/x-msdownload", new byte[]{1, 2, 3});
        final var cmd = CreatePropertyCommand.from(
                "Fazenda Boa Vista",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, List.of(), List.of(), List.of(attachment)
        );

        assertThrows(UnprocessableEntityException.class, () -> useCase.execute(cmd));
        verify(storageGateway, never()).store(any(), any(), any());
        verify(propertyGateway, never()).save(any());
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException quando arquivo excede o tamanho máximo, sem fazer upload")
    void shouldThrowWhenAttachmentExceedsMaxSize() {
        when(cityGateway.existsById(any())).thenReturn(true);
        when(producerGateway.existsById(any())).thenReturn(true);

        final var oversized = new byte[11 * 1024 * 1024];
        final var attachment = new AttachmentData("grande.pdf", "application/pdf", oversized);
        final var cmd = CreatePropertyCommand.from(
                "Fazenda Boa Vista",
                new BigDecimal("-25.43"), new BigDecimal("-49.27"),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                producerId, cityId, List.of(), List.of(), List.of(attachment)
        );

        assertThrows(UnprocessableEntityException.class, () -> useCase.execute(cmd));
        verify(storageGateway, never()).store(any(), any(), any());
        verify(propertyGateway, never()).save(any());
    }
}
