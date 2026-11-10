package br.gov.pr.idr.application.livestock.animal;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListAnimalUseCase")
class ListAnimalUseCaseTest {

    @Mock AnimalGateway animalGateway;
    @Mock LivestockNameLookup nameLookup;
    @Mock AnimalAccessPolicy accessPolicy;
    @InjectMocks ListAnimalUseCase useCase;

    private final UUID propertyId = UUID.randomUUID();

    private final BreedID breedId = BreedID.unique();

    private Pagination<Animal> somePagination() {
        final var animal = Animal.with(AnimalID.unique(), PropertyID.from(propertyId), null, breedId,
                "T", "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        return new Pagination<>(0, 10, 1, List.of(animal));
    }

    @Test
    void shouldListOnlyAnimalsOfAuthorizedProperty() {
        when(animalGateway.search(eq(PropertyID.from(propertyId)), any())).thenReturn(somePagination());
        when(nameLookup.breedNames(Set.of(breedId))).thenReturn(Map.of(breedId, "Gir"));

        final var output = useCase.execute(new ListAnimalCommand(propertyId, PropertySearchScope.unrestricted(),
                SearchQuery.from(0, 10, "", "identifier", "asc")));

        assertEquals(1, output.total());
        assertEquals("Gir", output.items().get(0).breedName());
    }

    @Test
    void shouldThrowForbiddenWhenAccessGuardDenies() {
        doThrow(ForbiddenException.with("x")).when(accessPolicy).ensureAccessToProperty(any(), any());

        assertThrows(ForbiddenException.class, () -> useCase.execute(new ListAnimalCommand(propertyId,
                PropertySearchScope.unrestricted(), SearchQuery.from(0, 10, "", "identifier", "asc"))));
        verify(animalGateway, never()).search(any(), any());
    }
}
