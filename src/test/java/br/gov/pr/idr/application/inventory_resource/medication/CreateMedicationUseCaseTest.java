package br.gov.pr.idr.application.inventory_resource.medication;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateMedicationUseCase")
class CreateMedicationUseCaseTest {

    @Mock MedicationGateway gateway;
    @Mock ProductGateway productGateway;
    @Mock ActivePrincipleGateway activePrincipleGateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks CreateMedicationUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();
    private final ActivePrincipleID activePrincipleId = ActivePrincipleID.unique();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), null, "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    private final Product catalogProduct = Product.with(ProductID.from(productId), "Ivomec", "Antiparasitário",
            activePrincipleId, ProductCategoryID.unique());

    private CreateMedicationCommand command(final UUID productId, final String productName,
                                            final String activePrinciple) {
        return new CreateMedicationCommand(propertyId, animalId, PropertySearchScope.unrestricted(), productId,
                productName, activePrinciple, LocalDate.now(), "10ml", "IM");
    }

    @Test
    @DisplayName("com produto do catálogo grava o nome e o princípio ativo do catálogo, ignorando os do cliente")
    void shouldSnapshotCatalogProduct() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(animal);
        when(productGateway.findById(ProductID.from(productId))).thenReturn(Optional.of(catalogProduct));
        when(activePrincipleGateway.findById(activePrincipleId))
                .thenReturn(Optional.of(ActivePrinciple.with(activePrincipleId, "Ivermectina")));
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(command(productId, "Nome do cliente", "Princípio do cliente"));

        assertEquals(productId, output.productId());
        assertEquals("Ivomec", output.productName());
        assertEquals("Ivermectina", output.activePrinciple());
        assertEquals("10ml", output.appliedDose());
        assertEquals("IM", output.applicationWay());
    }

    @Test
    @DisplayName("sem produto do catálogo grava o nome e o princípio ativo digitados")
    void shouldCreateWithTypedProduct() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(animal);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(command(null, "Vermífugo caseiro", "Alho"));

        assertNull(output.productId());
        assertEquals("Vermífugo caseiro", output.productName());
        assertEquals("Alho", output.activePrinciple());
        verifyNoInteractions(productGateway, activePrincipleGateway);
    }

    @Test
    @DisplayName("sem produto do catálogo e sem nome lança erro de validação")
    void shouldThrowWhenNoProductAndNoName() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(animal);

        final var exception = assertThrows(NotificationException.class,
                () -> useCase.execute(command(null, "  ", null)));

        assertEquals("O produto da medicação é obrigatório", exception.getErrors().getFirst().message());
        verify(gateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenReturn(animal);
        when(productGateway.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotificationException.class, () -> useCase.execute(command(productId, "Ivomec", null)));
        verify(gateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(ForbiddenException.with("x"));
        assertThrows(ForbiddenException.class, () -> useCase.execute(command(productId, null, null)));
    }
}
