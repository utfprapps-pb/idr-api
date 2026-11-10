package br.gov.pr.idr.application.livestock.animal_childbirth;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAnimalChildbirthByIdUseCase")
class GetAnimalChildbirthByIdUseCaseTest {

    @Mock AnimalChildbirthGateway gateway;
    @Mock LivestockNameLookup nameLookup;
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
    @InjectMocks GetAnimalChildbirthByIdUseCase useCase;

    private GetAnimalChildbirthByIdCommand command() {
        return new GetAnimalChildbirthByIdCommand(propertyId, animalId, childbirthId, PropertySearchScope.unrestricted());
    }

    @Test
    @DisplayName("devolve nome da raça e identificador do bezerro para exibição")
    void shouldReturnBreedNameAndCalfIdentifier() {
        final var calfId = UUID.randomUUID();
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(mother);
        when(gateway.findByIdAndAnimalId(AnimalChildbirthID.from(childbirthId), AnimalID.from(animalId)))
                .thenReturn(Optional.of(childbirthOf(animalId, calfId)));
        when(nameLookup.breedName(BreedID.from(breedId))).thenReturn("Jersey");
        when(nameLookup.animalIdentifier(AnimalID.from(calfId))).thenReturn("Bezerra 7");

        final var output = useCase.execute(command());

        assertEquals("Jersey", output.breedName());
        assertEquals(calfId, output.calfId());
        assertEquals("Bezerra 7", output.calfIdentifier());
    }

    @Test
    void shouldThrowNotFoundWhenChildbirthBelongsToAnotherAnimal() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(mother);
        when(gateway.findByIdAndAnimalId(AnimalChildbirthID.from(childbirthId), AnimalID.from(animalId)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(command()));
    }
}
