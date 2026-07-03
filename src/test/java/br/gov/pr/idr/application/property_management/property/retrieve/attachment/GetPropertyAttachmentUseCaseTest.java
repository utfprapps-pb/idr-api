package br.gov.pr.idr.application.property_management.property.retrieve.attachment;

import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetPropertyAttachmentUseCase")
class GetPropertyAttachmentUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @Mock StorageGateway storageGateway;
    @InjectMocks GetPropertyAttachmentUseCase useCase;

    private Property stubProperty() {
        return Property.with(PropertyID.unique(), "Fazenda",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                br.gov.pr.idr.domain.property_management.producer.ProducerID.unique(),
                br.gov.pr.idr.domain.property_management.city.CityID.unique(),
                List.of(), List.of());
    }

    @Test
    @DisplayName("deve retornar o conteúdo do anexo quando ele pertence à propriedade")
    void shouldReturnAttachmentContent() {
        final var property = stubProperty();
        final var attachment = PropertyAttachment.create(property.getId(), "contrato.pdf", "application/pdf", 10);
        property.addAttachment(attachment);
        when(propertyGateway.findById(any())).thenReturn(Optional.of(property));
        when(storageGateway.retrieve(attachment.getStorageKey())).thenReturn(new byte[]{1, 2, 3});

        final var command = GetPropertyAttachmentCommand.from(property.getId().id(), attachment.getId().id());
        final var output = useCase.execute(command);

        assertEquals("contrato.pdf", output.fileName());
        assertEquals("application/pdf", output.contentType());
        assertArrayEquals(new byte[]{1, 2, 3}, output.content());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando a propriedade não existe")
    void shouldThrowWhenPropertyNotFound() {
        when(propertyGateway.findById(any())).thenReturn(Optional.empty());
        final var command = GetPropertyAttachmentCommand.from(UUID.randomUUID(), UUID.randomUUID());

        assertThrows(NotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando o anexo não pertence à propriedade")
    void shouldThrowWhenAttachmentNotFound() {
        final var property = stubProperty();
        when(propertyGateway.findById(any())).thenReturn(Optional.of(property));

        final var command = GetPropertyAttachmentCommand.from(property.getId().id(), UUID.randomUUID());

        assertThrows(NotFoundException.class, () -> useCase.execute(command));
    }
}
