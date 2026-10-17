package br.gov.pr.idr.application.livestock.animal_purchases;

import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListAnimalPurchasesUseCase")
class ListAnimalPurchasesUseCaseTest {

    @Mock AnimalPurchasesGateway gateway;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks ListAnimalPurchasesUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final Animal animal = Animal.with(AnimalID.from(animalId), PropertyID.from(propertyId), null,
            BreedID.unique(), "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);

    @Test
    void shouldReturnEmptyPageWhenAnimalHasNoAnimalPurchases() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(animal);
        when(gateway.search(eq(AnimalID.from(animalId)), any())).thenReturn(new Pagination<>(0, 10, 0L, List.of()));
        final var output = useCase.execute(new ListAnimalPurchasesCommand(propertyId, animalId,
                "tecnico@idr.com", SearchQuery.from(0, 10, "", "datePurchase", "asc")));
        assertEquals(0, output.total());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(ForbiddenException.with("x"));
        assertThrows(ForbiddenException.class, () -> useCase.execute(new ListAnimalPurchasesCommand(propertyId,
                animalId, "tecnico@idr.com", SearchQuery.from(0, 10, "", "datePurchase", "asc"))));
        verify(gateway, never()).search(any(), any());
    }
}
