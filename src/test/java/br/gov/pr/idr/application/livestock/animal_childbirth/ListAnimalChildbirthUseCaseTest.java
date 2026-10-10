package br.gov.pr.idr.application.livestock.animal_childbirth;

import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListAnimalChildbirthUseCase")
class ListAnimalChildbirthUseCaseTest {

    @Mock AnimalChildbirthGateway gateway;
    @Mock BreedGateway breedGateway;
    @Mock AnimalGateway animalGateway;
    @Mock AnimalAccessPolicy accessPolicy;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID breedId = UUID.randomUUID();
    private final UUID childbirthId = UUID.randomUUID();
    private final Animal mother = animal(animalId, propertyId, "Mimosa");

    private static Animal animal(final UUID id, final UUID propertyId, final String identifier) {
        return Animal.with(AnimalID.from(id), PropertyID.from(propertyId), null, BreedID.unique(), "T",
                identifier, "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    }

    private AnimalChildbirth childbirthOf(final UUID ownerId, final UUID calfId) {
        return AnimalChildbirth.with(AnimalChildbirthID.from(childbirthId), AnimalID.from(ownerId),
                LocalDate.of(2024, 3, 10), ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE,
                BreedID.from(breedId), calfId == null ? null : AnimalID.from(calfId));
    }
    @InjectMocks ListAnimalChildbirthUseCase useCase;

    @Test
    @DisplayName("resolve raça e bezerro da página inteira em uma consulta cada; sem bezerro, não consulta animais")
    void shouldResolveNamesOncePerPage() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, "tecnico@idr.com")).thenReturn(mother);
        when(gateway.search(any(), any()))
                .thenReturn(new Pagination<>(0, 10, 1, List.of(childbirthOf(animalId, null))));
        when(breedGateway.findNamesByIds(Set.of(BreedID.from(breedId))))
                .thenReturn(Map.of(BreedID.from(breedId), "Gir"));

        final var output = useCase.execute(new ListAnimalChildbirthCommand(propertyId, animalId, "tecnico@idr.com",
                SearchQuery.from(0, 10, "", "childbirthDate", "asc")));

        assertEquals(1, output.total());
        assertEquals("Gir", output.items().get(0).breedName());
        assertNull(output.items().get(0).calfIdentifier());
        verify(breedGateway, times(1)).findNamesByIds(any());
        verify(animalGateway, never()).findAllById(any());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));

        assertThrows(NotFoundException.class, () -> useCase.execute(new ListAnimalChildbirthCommand(propertyId,
                animalId, "tecnico@idr.com", SearchQuery.from(0, 10, "", "childbirthDate", "asc"))));
        verify(gateway, never()).search(any(), any());
    }
}
