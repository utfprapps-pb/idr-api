package br.gov.pr.idr.infra.property_management.property.persistence;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("PropertyJPAEntity")
class PropertyJPAEntityTest {

    @Test
    @DisplayName("deve converter entidade JPA em agregado de domínio")
    void shouldConvertToDomain() {
        final var propertyId = PropertyID.unique();
        final var technicianId = UserID.unique();
        final var collaborator = PropertyCollaborator.create("João", "8h");
        final var property = Property.with(
                propertyId, "Fazenda",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.TEN, BigDecimal.ONE, 1.0, 2.0, 3.0, 4.0,
                ProducerID.unique(), CityID.unique(),
                List.of(technicianId), List.of(collaborator), null, null
        );
        property.addAttachment(PropertyAttachment.create(propertyId, "laudo.pdf", "application/pdf", 1024L));

        final var entity = PropertyJPAEntity.fromDomain(property);

        final var domain = entity.toDomain();

        assertEquals(property.getId(), domain.getId());
        assertEquals(property.getName(), domain.getName());
        assertEquals(property.getCoord(), domain.getCoord());
        assertEquals(property.getNakedAveragePrice(), domain.getNakedAveragePrice());
        assertEquals(property.getLeaseAveragePrice(), domain.getLeaseAveragePrice());
        assertEquals(property.getDairyCattleFarmingArea(), domain.getDairyCattleFarmingArea());
        assertEquals(property.getPerennialPastureArea(), domain.getPerennialPastureArea());
        assertEquals(property.getSummerPlowingArea(), domain.getSummerPlowingArea());
        assertEquals(property.getWinterPlowingArea(), domain.getWinterPlowingArea());
        assertEquals(property.getProducerId(), domain.getProducerId());
        assertEquals(property.getCityId(), domain.getCityId());
        assertEquals(property.getTechnicianIds(), domain.getTechnicianIds());
        assertEquals(property.getVersion(), domain.getVersion());
        assertEquals(property.getUpdatedAt(), domain.getUpdatedAt());

        assertEquals(1, domain.getCollaborators().size());
        assertEquals(collaborator.getId(), domain.getCollaborators().get(0).getId());
        assertEquals(collaborator.getName(), domain.getCollaborators().get(0).getName());
        assertEquals(collaborator.getHoursPerDay(), domain.getCollaborators().get(0).getHoursPerDay());

        assertEquals(1, domain.getAttachments().size());
        assertEquals("laudo.pdf", domain.getAttachments().get(0).getFileName());
    }
}
