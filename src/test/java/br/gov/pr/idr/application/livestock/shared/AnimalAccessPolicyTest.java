package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalAccessPolicy")
class AnimalAccessPolicyTest {

    @Mock
    private PropertyGateway propertyGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private AnimalGateway animalGateway;

    private AnimalAccessPolicy guard;

    private final PropertyID propertyIdA = PropertyID.unique();
    private final PropertyID propertyIdB = PropertyID.unique();
    private final AnimalID animalId = AnimalID.unique();

    private final User adminUser = userWithRole(UserRole.ADMIN);
    private final Animal animalOfPropertyA = animalOf(propertyIdA);
    private final Animal animalOfPropertyB = animalOf(propertyIdB);

    @BeforeEach
    void setUp() {
        guard = new AnimalAccessPolicy(propertyGateway, userGateway, animalGateway);
    }

    private static User userWithRole(final UserRole role) {
        return User.create("Usuário", "user-" + UUID.randomUUID(), Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), null, null, null, null, null, null, null,
                Set.of(Permission.create(role, false, Set.of(), Set.of())));
    }

    private Animal animalOf(final PropertyID ownerPropertyId) {
        return Animal.with(animalId, ownerPropertyId, null, BreedID.unique(), "Bovino", "IDENT-1",
                "F", "NORMAL", null, null, null, null, null);
    }

    @Test
    @DisplayName("ensureAccessToProperty lança Forbidden quando a propriedade está fora do escopo")
    void ensureAccessToPropertyThrowsForbiddenWhenScopeDenies() {
        final var technician = userWithRole(UserRole.TECNICO);
        when(userGateway.findByUsername("tecnico@idr.com")).thenReturn(Optional.of(technician));
        when(propertyGateway.existsById(propertyIdA)).thenReturn(true);
        when(propertyGateway.isAccessibleBy(propertyIdA, PropertySearchScope.byTechnician(technician.getId())))
                .thenReturn(false);

        assertThrows(ForbiddenException.class,
                () -> guard.ensureAccessToProperty(propertyIdA.id(), "tecnico@idr.com"));
    }

    @Test
    @DisplayName("ensureAccessToProperty lança NotFound quando a propriedade não existe, sem checar o escopo")
    void ensureAccessToPropertyThrowsNotFoundWhenPropertyDoesNotExist() {
        when(userGateway.findByUsername(any())).thenReturn(Optional.of(adminUser));
        when(propertyGateway.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> guard.ensureAccessToProperty(UUID.randomUUID(), "admin@idr.com"));
        verify(propertyGateway, never()).isAccessibleBy(any(), any());
    }

    @Test
    @DisplayName("ensureAccessToAnimal lança NotFound quando o animal pertence a outra propriedade")
    void ensureAccessToAnimalThrowsNotFoundWhenAnimalBelongsToAnotherProperty() {
        when(userGateway.findByUsername(any())).thenReturn(Optional.of(adminUser));
        when(propertyGateway.existsById(propertyIdA)).thenReturn(true);
        when(propertyGateway.isAccessibleBy(propertyIdA, PropertySearchScope.unrestricted())).thenReturn(true);
        when(animalGateway.findById(animalId)).thenReturn(Optional.of(animalOfPropertyB));

        assertThrows(NotFoundException.class,
                () -> guard.ensureAccessToAnimal(propertyIdA.id(), animalId.id(), "admin@idr.com"));
    }

    @Test
    @DisplayName("ensureAccessToAnimal retorna o animal quando ele pertence à propriedade autorizada")
    void ensureAccessToAnimalReturnsAnimalWhenItBelongsToTheAuthorizedProperty() {
        when(userGateway.findByUsername(any())).thenReturn(Optional.of(adminUser));
        when(propertyGateway.existsById(propertyIdA)).thenReturn(true);
        when(propertyGateway.isAccessibleBy(propertyIdA, PropertySearchScope.unrestricted())).thenReturn(true);
        when(animalGateway.findById(animalId)).thenReturn(Optional.of(animalOfPropertyA));

        final var result = guard.ensureAccessToAnimal(propertyIdA.id(), animalId.id(), "admin@idr.com");

        assertEquals(animalId, result.getId());
    }
}
