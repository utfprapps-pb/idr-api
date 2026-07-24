package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Property — Aggregate")
class PropertyTest {

    private static final Coord COORD = Coord.from(new BigDecimal("-25.4290"), new BigDecimal("-49.2671"));
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final List<UserID> NO_TECHNICIANS = List.of();
    private static final List<PropertyCollaborator> NO_COLLABORATORS = List.of();
    private ProducerID producerId;
    private CityID cityId;

    @BeforeEach
    void setUp() {
        producerId = ProducerID.unique();
        cityId = CityID.unique();
    }

    private Property validProperty() {
        return Property.create(
                "Fazenda Boa Vista",
                COORD,
                ZERO,
                ZERO,
                0.0,
                0.0,
                0.0,
                0.0,
                producerId,
                cityId,
                NO_TECHNICIANS,
                NO_COLLABORATORS
        );
    }

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar propriedade com dados válidos")
        void shouldCreateValidProperty() {
            final var property = validProperty();

            assertNotNull(property.getId());
            assertEquals("Fazenda Boa Vista", property.getName());
            assertEquals(COORD, property.getCoord());
            assertEquals(producerId, property.getProducerId());
            assertEquals(cityId, property.getCityId());
        }

        @Test
        @DisplayName("deve rejeitar propriedade sem nome")
        void shouldRejectNullName() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create(null, COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Nome")));
        }

        @Test
        @DisplayName("deve rejeitar propriedade com nome vazio")
        void shouldRejectBlankName() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("   ", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Nome")));
        }

        @Test
        @DisplayName("deve rejeitar propriedade sem cidade")
        void shouldRejectNullCity() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0, producerId, null, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Cidade")));
        }

        @Test
        @DisplayName("deve rejeitar propriedade sem produtor")
        void shouldRejectNullProducer() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0, null, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Produtor")));
        }

        @Test
        @DisplayName("deve rejeitar preço de arrendamento nu negativo")
        void shouldRejectNegativeNakedPrice() {
            final var negativeOne = new BigDecimal("-1");
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, negativeOne, ZERO, 0.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("arrendamento nu")));
        }

        @Test
        @DisplayName("deve rejeitar preço de arrendamento negativo")
        void shouldRejectNegativeLeasePrice() {
            final var negativeOne = new BigDecimal("-1");
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, negativeOne, 0.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("arrendamento")));
        }

        @Test
        @DisplayName("deve rejeitar preço de arrendamento nu nulo")
        void shouldRejectNullNakedPrice() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, null, ZERO, 0.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("arrendamento nu")));
        }

        @Test
        @DisplayName("deve rejeitar preço de arrendamento nulo")
        void shouldRejectNullLeasePrice() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, null, 0.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("arrendamento")));
        }

        @Test
        @DisplayName("deve rejeitar área de pecuária leiteira negativa")
        void shouldRejectNegativeDairyArea() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, -1.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("pecuária leiteira")));
        }

        @Test
        @DisplayName("deve rejeitar área de pecuária leiteira nula")
        void shouldRejectNullDairyArea() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, null, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("pecuária leiteira")));
        }

        @Test
        @DisplayName("deve rejeitar área de pastagem perene nula")
        void shouldRejectNullPerennialPasture() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, null, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("pastagem perene")));
        }

        @Test
        @DisplayName("deve rejeitar área de pastagem perene negativa")
        void shouldRejectNegativePerennialPasture() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, -1.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("pastagem perene")));
        }

        @Test
        @DisplayName("deve rejeitar área de plantio de verão nula")
        void shouldRejectNullSummerPlowing() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, null, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("plantio de verão")));
        }

        @Test
        @DisplayName("deve rejeitar área de plantio de verão negativa")
        void shouldRejectNegativeSummerPlowing() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, -1.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("plantio de verão")));
        }

        @Test
        @DisplayName("deve rejeitar área de plantio de inverno nula")
        void shouldRejectNullWinterPlowing() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, null, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("plantio de inverno")));
        }

        @Test
        @DisplayName("deve rejeitar área de plantio de inverno negativa")
        void shouldRejectNegativeWinterPlowing() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Property.create("Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, -1.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("plantio de inverno")));
        }

        @Test
        @DisplayName("deve criar propriedade com colaboradores e técnicos")
        void shouldCreateWithCollaboratorsAndTechnicians() {
            final var collaborator = PropertyCollaborator.create("Carlos", "8");
            final var technicianId = UserID.unique();

            final var property = Property.create(
                    "Fazenda Feliz",
                    COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0,
                    producerId, cityId,
                    List.of(technicianId),
                    List.of(collaborator)
            );

            assertEquals(1, property.getCollaborators().size());
            assertEquals(1, property.getTechnicianIds().size());
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir propriedade a partir de dados persistidos")
        void shouldReconstitute() {
            final var id = PropertyID.unique();
            final var property = Property.with(id, "Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0,
                    producerId, cityId, List.of(), List.of(), null, null);

            assertEquals(id, property.getId());
        }

        @Test
        @DisplayName("deve iniciar com lista de anexos vazia quando construída com anexos nulos")
        void shouldStartWithEmptyAttachmentsWhenAttachmentsIsNull() {
            final var id = PropertyID.unique();
            final var property = new Property(id, "Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0,
                    producerId, cityId, List.of(), List.of(), null, null, null);

            assertNotNull(property.getAttachments());
            assertTrue(property.getAttachments().isEmpty());
        }

        @Test
        @DisplayName("deve copiar os anexos informados quando construída com lista de anexos não nula")
        void shouldCopyAttachmentsWhenAttachmentsIsNotNull() {
            final var id = PropertyID.unique();
            final var attachment = PropertyAttachment.create(id, "laudo.pdf", "application/pdf", 1024L);

            final var property = new Property(id, "Fazenda", COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0,
                    producerId, cityId, List.of(), List.of(), null, null, List.of(attachment));

            assertEquals(1, property.getAttachments().size());
            assertEquals(attachment, property.getAttachments().get(0));
        }
    }

    @Nested
    @DisplayName("Atualização")
    class Update {

        @Test
        @DisplayName("deve atualizar dados da propriedade")
        void shouldUpdateProperty() {
            final var property = validProperty();
            final var newProducer = ProducerID.unique();

            property.update("Fazenda Atualizada", COORD, new BigDecimal("100.00"), new BigDecimal("50.00"),
                    10.0, 5.0, 3.0, 2.0, newProducer, cityId, List.of(), List.of());

            assertEquals("Fazenda Atualizada", property.getName());
            assertEquals(newProducer, property.getProducerId());
        }

        @Test
        @DisplayName("deve rejeitar atualização com nome nulo")
        void shouldRejectUpdateWithNullName() {
            final var property = validProperty();
            assertThrows(NotificationException.class,
                    () -> property.update(null, COORD, ZERO, ZERO, 0.0, 0.0, 0.0, 0.0, producerId, cityId, NO_TECHNICIANS, NO_COLLABORATORS));
        }
    }
}
