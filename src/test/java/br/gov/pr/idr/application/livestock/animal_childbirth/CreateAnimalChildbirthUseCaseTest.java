package br.gov.pr.idr.application.livestock.animal_childbirth;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.application.livestock.animal_childbirth.create.CreateAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.create.CreateAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAnimalChildbirthUseCase")
class CreateAnimalChildbirthUseCaseTest {

    @Mock AnimalChildbirthGateway gateway;
    @Mock BreedGateway breedGateway;
    @Mock AnimalGateway animalGateway;
    @Mock AnimalAccessPolicy accessPolicy;

    private final UUID propertyId = UUID.randomUUID();
    private final UUID animalId = UUID.randomUUID();
    private final UUID breedId = UUID.randomUUID();
    private final Animal mother = animal(animalId, propertyId, "Mimosa");

    private static Animal animal(final UUID id, final UUID propertyId, final String identifier) {
        return Animal.with(AnimalID.from(id), PropertyID.from(propertyId), null, BreedID.unique(), "T",
                identifier, "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
    }

    CreateAnimalChildbirthUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateAnimalChildbirthUseCase(gateway, new LivestockReferencePolicy(breedGateway, animalGateway), accessPolicy);
    }

    private CreateAnimalChildbirthCommand command(final UUID calfId) {
        return new CreateAnimalChildbirthCommand(propertyId, animalId, PropertySearchScope.unrestricted(), LocalDate.of(2024, 3, 10),
                ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE, breedId, calfId);
    }

    @Test
    @DisplayName("cria parto sem bezerro vinculado (bezerro fora do rebanho)")
    void shouldCreateWithoutCalf() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(mother);
        when(breedGateway.existsById(BreedID.from(breedId))).thenReturn(true);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(command(null));

        assertEquals(animalId, output.animalId());
        assertEquals(breedId, output.breedId());
        assertNull(output.calfId());
        verify(animalGateway, never()).existsByIdAndPropertyId(any(), any());
    }

    @Test
    @DisplayName("cria parto vinculando bezerro da mesma propriedade")
    void shouldCreateWithCalfOfSameProperty() {
        final var calfId = UUID.randomUUID();
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(mother);
        when(breedGateway.existsById(BreedID.from(breedId))).thenReturn(true);
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(calfId), PropertyID.from(propertyId)))
                .thenReturn(true);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertEquals(calfId, useCase.execute(command(calfId)).calfId());
    }

    @Test
    @DisplayName("rejeita bezerro inexistente ou de outra propriedade (uma única consulta cobre os dois)")
    void shouldRejectCalfOutsideTheProperty() {
        final var calfId = UUID.randomUUID();
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(mother);
        when(breedGateway.existsById(BreedID.from(breedId))).thenReturn(true);
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(calfId), PropertyID.from(propertyId)))
                .thenReturn(false);

        assertThrows(NotificationException.class, () -> useCase.execute(command(calfId)));
        verify(gateway, never()).save(any());
    }

    @Test
    @DisplayName("rejeita a própria mãe como bezerro")
    void shouldRejectMotherAsItsOwnCalf() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(mother);
        when(breedGateway.existsById(BreedID.from(breedId))).thenReturn(true);
        when(animalGateway.existsByIdAndPropertyId(AnimalID.from(animalId), PropertyID.from(propertyId)))
                .thenReturn(true);

        assertThrows(NotificationException.class, () -> useCase.execute(command(animalId)));
        verify(gateway, never()).save(any());
    }

    @Test
    @DisplayName("rejeita raça inexistente")
    void shouldRejectUnknownBreed() {
        when(accessPolicy.ensureAccessToAnimal(propertyId, animalId, PropertySearchScope.unrestricted())).thenReturn(mother);
        when(breedGateway.existsById(BreedID.from(breedId))).thenReturn(false);

        assertThrows(NotificationException.class, () -> useCase.execute(command(null)));
        verify(gateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenAnimalIsNotAccessible() {
        when(accessPolicy.ensureAccessToAnimal(any(), any(), any())).thenThrow(NotFoundException.with("x"));

        assertThrows(NotFoundException.class, () -> useCase.execute(command(null)));
        verify(gateway, never()).save(any());
    }
}
