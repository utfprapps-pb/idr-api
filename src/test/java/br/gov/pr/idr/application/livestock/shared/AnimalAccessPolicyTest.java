package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalAccessPolicy")
class AnimalAccessPolicyTest {

    @Mock
    private PropertyGateway propertyGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private CityGateway cityGateway;

    @Mock
    private AnimalGateway animalGateway;

    private AnimalAccessPolicy guard;

    private final PropertyID propertyId = PropertyID.unique();
    private final PropertyID propertyIdA = PropertyID.unique();
    private final PropertyID propertyIdB = PropertyID.unique();
    private final AnimalID animalId = AnimalID.unique();

    private final User technicianUser = userWithRole(UserRole.TECNICO);
    private final User adminUser = userWithRole(UserRole.ADMIN);

    private final Property propertyA = propertyWithId(propertyIdA);
    private final Animal animalOfPropertyA = animalOf(propertyIdA);
    private final Animal animalOfPropertyB = animalOf(propertyIdB);

    @BeforeEach
    void setUp() {
        guard = new AnimalAccessPolicy(propertyGateway, userGateway, cityGateway, animalGateway);
    }

    private static User userWithRole(final UserRole role) {
        return User.create("Usuário", "user-" + UUID.randomUUID(), Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), null, null, null, null, null, null, null,
                Set.of(Permission.create(role, false, Set.of(), Set.of())));
    }

    private static Property propertyWithId(final PropertyID id) {
        return Property.with(id, "Fazenda", null, null, null, null, null, null, null,
                ProducerID.unique(), CityID.unique(), List.of(), List.of(), null, null);
    }

    private static Property propertyWithTechnicians(final List<UserID> technicianIds) {
        return Property.with(PropertyID.unique(), "Fazenda", null, null, null, null, null, null, null,
                ProducerID.unique(), CityID.unique(), technicianIds, List.of(), null, null);
    }

    private Animal animalOf(final PropertyID ownerPropertyId) {
        return Animal.with(animalId, ownerPropertyId, null, BreedID.unique(), "Bovino", "IDENT-1",
                "F", "NORMAL", null, null, null, null, null);
    }

    @Test
    @DisplayName("ensureAccessToProperty lança Forbidden quando o escopo nega acesso")
    void ensureAccessToPropertyThrowsForbiddenWhenScopeDenies() {
        when(userGateway.findByUsername("tecnico@idr.com")).thenReturn(Optional.of(technicianUser));
        when(propertyGateway.findById(propertyId))
                .thenReturn(Optional.of(propertyWithTechnicians(List.of(UserID.unique()))));

        assertThrows(ForbiddenException.class,
                () -> guard.ensureAccessToProperty(propertyId.id(), "tecnico@idr.com"));
    }

    @Test
    @DisplayName("ensureAccessToProperty lança NotFound quando a propriedade não existe")
    void ensureAccessToPropertyThrowsNotFoundWhenPropertyDoesNotExist() {
        when(userGateway.findByUsername(any())).thenReturn(Optional.of(adminUser));
        when(propertyGateway.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> guard.ensureAccessToProperty(UUID.randomUUID(), "admin@idr.com"));
    }

    @Test
    @DisplayName("ensureAccessToAnimal lança NotFound quando o animal pertence a outra propriedade")
    void ensureAccessToAnimalThrowsNotFoundWhenAnimalBelongsToAnotherProperty() {
        when(userGateway.findByUsername(any())).thenReturn(Optional.of(adminUser));
        when(propertyGateway.findById(propertyIdA)).thenReturn(Optional.of(propertyA));
        when(animalGateway.findById(animalId)).thenReturn(Optional.of(animalOfPropertyB));

        assertThrows(NotFoundException.class,
                () -> guard.ensureAccessToAnimal(propertyIdA.id(), animalId.id(), "admin@idr.com"));
    }

    @Test
    @DisplayName("ensureAccessToAnimal retorna o animal quando ele pertence à propriedade autorizada")
    void ensureAccessToAnimalReturnsAnimalWhenItBelongsToTheAuthorizedProperty() {
        when(userGateway.findByUsername(any())).thenReturn(Optional.of(adminUser));
        when(propertyGateway.findById(propertyIdA)).thenReturn(Optional.of(propertyA));
        when(animalGateway.findById(animalId)).thenReturn(Optional.of(animalOfPropertyA));

        final var result = guard.ensureAccessToAnimal(propertyIdA.id(), animalId.id(), "admin@idr.com");

        assertEquals(animalId, result.getId());
    }
}
