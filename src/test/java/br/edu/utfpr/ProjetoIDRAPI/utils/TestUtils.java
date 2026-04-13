package br.edu.utfpr.ProjetoIDRAPI.utils;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.breed.Breed;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.City;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.LandProduct;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.Medication;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.entity.permission.Permission;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.Product;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyarea.PropertyArea;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.enums.AnimalSize;
import br.edu.utfpr.ProjetoIDRAPI.enums.CultureType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TestUtils {

    public static User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserPermissions(Set.of());
        return user;
    }

    public static Region createValidRegion() {
        return Region.builder()
                .name("Region Name")
                .build();
    }

    public static City createValidCity(Region region) {
        return City.builder()
                .name("City Name")
                .cityRegion(region)
                .build();
    }

    public static Permission createValidPermission() {
        return Permission.builder()
                .name("ROLE_USER")
                .build();
    }

    public static User createValidUser() {
        return createValidUser("1");
    }

    public static User createValidUser(String suffix) {
        return User.builder()
                .username("test" + suffix + "@test.com")
                .displayName("Test User " + suffix)
                .password("password")
                .cpf("1111111111" + suffix)
                .city("Test City")
                .cep("12345-678")
                .street("Test Street")
                .houseNumber("123")
                .phone("123456789")
                .professionalRegister("12345")
                .graduationYear("2020")
                .userPermissions(Set.of())
                .build();
    }

    public static Breed createValidBreed() {
        return Breed.builder()
                .breedName("Breed Name")
                .build();
    }

    public static PropertyArea createValidPropertyArea() {
        return PropertyArea.builder()
                .dairyCattleFarming(10.0)
                .perennialPasture(20.0)
                .summerPlowing(30.0)
                .winterPlowing(40.0)
                .build();
    }

    public static PropertyCollaborator createValidPropertyCollaborator() {
        return PropertyCollaborator.builder()
                .collaboratorName("Collaborator Name")
                .workHours(8)
                .workDays(5)
                .build();
    }

    public static PropertyTechnician createValidPropertyTechnician(User user) {
        return PropertyTechnician.builder()
                .user(user)
                .build();
    }

    public static Property createValidProperty(User owner, List<PropertyTechnician> technicians) {
        Property property = Property.builder()
                .user(owner)
                .ocupationArea("Occupation Area")
                .totalArea(BigDecimal.valueOf(100.0))
                .latitude(BigInteger.valueOf(12345))
                .longitude(BigInteger.valueOf(67890))
                .leased(false)
                .name("Property Name")
                .city("Property City")
                .state("Property State")
                .nakedAveragePrice(100000.0)
                .leaseAveragePrice(5000.0)
                .farmer("Farmer Name")
                .build();

        PropertyArea area = createValidPropertyArea();
        area.setProperty(property);


        PropertyCollaborator collaborator = createValidPropertyCollaborator();
        collaborator.setProperty(property);

        technicians.forEach(tech -> tech.setProperty(property));

        property.setArea(area);
        property.setCollaborators(List.of(collaborator));
        property.setTechnicians(technicians);

        return property;
    }

    public static Animal createValidAnimal(Property property, Breed breed) {
        return Animal.builder()
                .property(property)
                .animalMother(null)
                .breed(breed)
                .type("Bovine")
                .identifier("123")
                .gender("F")
                .bornCondition("Vivo")
                .bornDate(LocalDate.now())
                .bornWeight(30.0f)
                .size(AnimalSize.SMALL)
                .previousWeight(25.0f)
                .currentWeight(30.0f)
                .ecc(3.5f)
                .build();
    }

    public static Plague createValidPlague() {
        return Plague.builder().plagueName("Ferrugem").build();
    }

    public static Culture createValidCulture() {
        return Culture.builder().cultureName("Soja").cultureType(CultureType.VOLUMOSO).build();
    }

    public static Disease createValidDisease() {
        return Disease.builder().diseaseName("Febre Aftosa").build();
    }

    public static Product createValidProduct() {
        return Product.builder().name("Herbicida").description("Mata mato").applicationWay("Pulverização").build();
    }

    public static Medication createValidMedication() {
        return Medication.builder().appliedDose("10ml").applicationWay("Injeção").build();
    }

    public static LandProduct createValidLandProduct(Property property) {
        return LandProduct.builder().usedFor("Venda").property(property).build();
    }

    public static VegetablePlague createValidVegetablePlague(Plague plague, Culture culture, Property property) {
        return VegetablePlague.builder()
                .plague(plague)
                .culture(culture)
                .property(property)
                .infestationType("Alta")
                .build();
    }

    public static VegetableDisease createValidVegetableDisease(Disease disease, Culture culture, Property property) {
        return VegetableDisease.builder()
                .disease(disease)
                .property(property)
                .infestationType("Baixa")
                .culture(culture)
                .build();
    }

    public static ForageDisponibility createValidForageDisponibility(Property property) {
        return ForageDisponibility.builder().property(property).build();
    }

    public static PerennialAnualForage createValidPerennialAnualForage(Property property) {
        return PerennialAnualForage.builder().property(property).build();
    }

    public static PropertyEquipImprove createValidPropertyEquipImprove(Property property) {
        return PropertyEquipImprove.builder()
                .name("Trator")
                .type("Equipamento")
                .property(property)
                .build();
    }
}