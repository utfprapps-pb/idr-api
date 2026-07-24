package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyOutput;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreatePropertyOutput")
class CreatePropertyOutputTest {

    private Property stubProperty() {
        return Property.with(PropertyID.unique(), "Fazenda Boa Vista",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                ProducerID.from(UUID.randomUUID()), CityID.from(UUID.randomUUID()),
                List.of(), List.of(), null, null);
    }

    @Test
    @DisplayName("deve mapear propriedade sem anexos para uma lista vazia")
    void shouldMapPropertyWithoutAttachments() {
        final var property = stubProperty();

        final var output = CreatePropertyOutput.from(property);

        assertEquals(property.getId().id(), output.id());
        assertEquals(property.getName(), output.name());
        assertTrue(output.attachments().isEmpty());
    }

    @Test
    @DisplayName("deve mapear anexos da propriedade para AttachmentSummary")
    void shouldMapPropertyAttachments() {
        final var property = stubProperty();
        final var attachment = PropertyAttachment.create(
                property.getId(), "documento.pdf", "application/pdf", 1024);
        property.addAttachment(attachment);

        final var output = CreatePropertyOutput.from(property);

        assertEquals(1, output.attachments().size());
        final var summary = output.attachments().getFirst();
        assertEquals(attachment.getId().id(), summary.id());
        assertEquals("documento.pdf", summary.fileName());
        assertEquals("application/pdf", summary.contentType());
        assertEquals(1024L, summary.sizeBytes());
    }
}
