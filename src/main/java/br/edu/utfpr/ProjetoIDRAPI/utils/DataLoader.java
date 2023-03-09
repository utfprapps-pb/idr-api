package br.edu.utfpr.ProjetoIDRAPI.utils;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.edu.utfpr.ProjetoIDRAPI.controller.AnimalController;
import br.edu.utfpr.ProjetoIDRAPI.controller.CityController;
import br.edu.utfpr.ProjetoIDRAPI.controller.ForageDisponibilityController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PerennialAnualForageController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PlagueDiseaseController;
import br.edu.utfpr.ProjetoIDRAPI.controller.ProductUseController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PropertyCollaboratorController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PropertyController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PropertyEquipImproveController;
import br.edu.utfpr.ProjetoIDRAPI.controller.RegionController;
import br.edu.utfpr.ProjetoIDRAPI.controller.UserController;
import br.edu.utfpr.ProjetoIDRAPI.controller.VegetableController;
import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.dto.UserDto;
import br.edu.utfpr.ProjetoIDRAPI.dto.VegetableDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.model.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.model.PlagueDisease;
import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;

//Classe responsavel por adicionar dados "fixos" ao banco de dados
@Component
public class DataLoader implements CommandLineRunner{
	@Autowired
	private UserController userController;
	@Autowired
	private PropertyController propertyController;
	@Autowired
	private AnimalController animalController;
	@Autowired
	private VegetableController vegetableController;
	@Autowired
	private RegionController regionController;
	@Autowired
	private PropertyEquipImproveController propertyEquipImproveController;
	@Autowired
	private ForageDisponibilityController forageDisponibilityController;
	@Autowired
	private PropertyCollaboratorController propertyCollaboratorController;
	@Autowired
	private ProductUseController productUseController;
	@Autowired
	private PlagueDiseaseController plagueDiseaseController;
	@Autowired
	private PerennialAnualForageController perennialAnualForageController;
	@Autowired
	private CityController cityController;
	
	@Override
	public void run(String... args) throws Exception {
		createUsers();
		createProperties();
		createRegions();
		createCities();
		createVegetables();
		createPropertyEquipImprove();
		createCollaborators();
		createProductsUses();
		createPlaguesDiseases();
		createForages();
		createForageDisponibility();
		createAnimals();
	}
	
	private void createUsers() {
		User user1 = new User(); 
		user1.setUsername("Fulano 1");  
		user1.setCpf("123");
		user1.setPhone("1111");
		user1.setProfessionalRegister("1111");
		
		User user2 = new User(); 
		user2.setUsername("Fulano 2");
		user2.setCpf("123");
		user2.setPhone("2222");
		user2.setProfessionalRegister("2222");
		
		User user3 = new User(); 
		user3.setUsername("Fulano 3");
		user3.setCpf("123");
		user3.setPhone("3333");
		user3.setProfessionalRegister("3333");
        
        userController.createRegister(user1);
        userController.createRegister(user2);
        userController.createRegister(user3);
	}

	private void createProperties() {
		Property property1 = new Property();
		property1.setUser(convertDtoToUser(userController.findByName("Fulano 1")));
		property1.setOcupationArea("Ocupation Area1");
		property1.setLeased(true);

		Property property2 = new Property();
		property2.setUser(convertDtoToUser(userController.findByName("Fulano 1")));
		property2.setOcupationArea("Ocupation Area2");
		property2.setLeased(true);

		Property property3 = new Property();
		property3.setUser(convertDtoToUser(userController.findByName("Fulano 2")));
		property3.setOcupationArea("Ocupation Area3");
		property3.setLeased(true);

		propertyController.createRegister(property1);
		propertyController.createRegister(property2);
		propertyController.createRegister(property3);
	}
	
	private void createRegions() {
		Region region1 = new Region(1l, "Region 1");
		Region region2 = new Region(2l, "Region 2");
		Region region3 = new Region(3l, "Region 3");
		
		regionController.createRegister(region1);
		regionController.createRegister(region2);
		regionController.createRegister(region3);
	}
	
	private void createCities() {
		City city1 = new City(1l, "City 1", convertResponseEntityToRegion(regionController.findByName("Region 1")));
		City city2 = new City(2l, "City 2", convertResponseEntityToRegion(regionController.findByName("Region 2")));
		City city3 = new City(3l, "City 3", convertResponseEntityToRegion(regionController.findByName("Region 3")));
		
		cityController.createRegister(city1);
		cityController.createRegister(city2);
		cityController.createRegister(city3);
	}
	
	private void createVegetables() {
		Vegetable vegetable1 = new Vegetable(1l,convertToProperty(propertyController.findOne(2l)),"Vegetable 1");
		Vegetable vegetable2 = new Vegetable(2l,convertToProperty(propertyController.findOne(2l)),"Vegetable 2");
		Vegetable vegetable3 = new Vegetable(3l,convertToProperty(propertyController.findOne(3l)),"Vegetable 3");
		
		vegetableController.createRegister(vegetable1);
		vegetableController.createRegister(vegetable2);
		vegetableController.createRegister(vegetable3);
	}
	
	private void createPropertyEquipImprove() {
        PropertyEquipImprove equipImprove1 = new PropertyEquipImprove();
        equipImprove1.setType("Test type");
        equipImprove1.setName("Equipament 1");
        equipImprove1.setProperty(convertToProperty(propertyController.findOne(3l)));
        
        PropertyEquipImprove equipImprove2 = new PropertyEquipImprove();
        equipImprove2.setType("Test type");
        equipImprove2.setName("Equipament 2");
        equipImprove2.setProperty(convertToProperty(propertyController.findOne(1l)));
        
        PropertyEquipImprove equipImprove3 = new PropertyEquipImprove();
        equipImprove3.setType("Test type");
        equipImprove3.setName("Equipament 3");
        equipImprove3.setProperty(convertToProperty(propertyController.findOne(1l)));

        propertyEquipImproveController.createRegister(equipImprove1);
        propertyEquipImproveController.createRegister(equipImprove2);
        propertyEquipImproveController.createRegister(equipImprove3);
    }
	
	private void createCollaborators() {
        PropertyCollaborator collaborator1 = new PropertyCollaborator();
        collaborator1.setCollaboratorName("collaborator 1");
        collaborator1.setWorkHours(12);
        collaborator1.setWorkDays(7);
        collaborator1.setProperty(convertToProperty(propertyController.findOne(2l)));
        
        PropertyCollaborator collaborator2 = new PropertyCollaborator();
        collaborator2.setCollaboratorName("collaborator 2");
        collaborator2.setWorkHours(8);
        collaborator2.setWorkDays(7);
        collaborator2.setProperty(convertToProperty(propertyController.findOne(3l)));
        
        PropertyCollaborator collaborator3 = new PropertyCollaborator();
        collaborator3.setCollaboratorName("collaborator 3");
        collaborator3.setWorkHours(8);
        collaborator3.setWorkDays(7);
        collaborator3.setProperty(convertToProperty(propertyController.findOne(1l)));
        
        propertyCollaboratorController.createRegister(collaborator1);
        propertyCollaboratorController.createRegister(collaborator2);
        propertyCollaboratorController.createRegister(collaborator3);
    }
	

	private void createProductsUses() {
        LocalDate date = LocalDate.parse("2022-06-23");
        ProductUse productUse1 = new ProductUse();
        productUse1.setProperty(convertToProperty(propertyController.findOne(1l)));
        productUse1.setUsedFor("UsedFor details 1");
        productUse1.setQuantity(20);
        productUse1.setUseDate(date);
        
        ProductUse productUse2 = new ProductUse();
        productUse2.setProperty(convertToProperty(propertyController.findOne(3l)));
        productUse2.setUsedFor("UsedFor details 2");
        productUse2.setQuantity(20);
        productUse2.setUseDate(date);
        
        ProductUse productUse3 = new ProductUse();
        productUse3.setProperty(convertToProperty(propertyController.findOne(2l)));
        productUse3.setUsedFor("UsedFor details 3");
        productUse3.setQuantity(20);
        productUse3.setUseDate(date);
        
        productUseController.createRegister(productUse1);
        productUseController.createRegister(productUse2);
        productUseController.createRegister(productUse3);
    }

	private void createPlaguesDiseases() {
		PlagueDisease plagueDisease1 = new PlagueDisease();
		plagueDisease1.setIdVegetable(convertDtoToVegetable(vegetableController.findByName("Vegetable 2")));
		plagueDisease1.setIdentification("ident 1");
		
		PlagueDisease plagueDisease2 = new PlagueDisease();
		plagueDisease2.setIdVegetable(convertDtoToVegetable(vegetableController.findByName("Vegetable 2")));
		plagueDisease2.setIdentification("ident 2");
		
		PlagueDisease plagueDisease3 = new PlagueDisease();
		plagueDisease3.setIdVegetable(convertDtoToVegetable(vegetableController.findByName("Vegetable 3")));
		plagueDisease3.setIdentification("ident 3");
		
		plagueDiseaseController.createRegister(plagueDisease1);
		plagueDiseaseController.createRegister(plagueDisease2);
		plagueDiseaseController.createRegister(plagueDisease3);
	}
	
	private void createForages() {
        PerennialAnualForage perennialAnualForage1 = new PerennialAnualForage();
        perennialAnualForage1.setForage("Forage 1");
        perennialAnualForage1.setNote("Note 1");
        perennialAnualForage1.setProperty(convertToProperty(propertyController.findOne(2l)));
        
        PerennialAnualForage perennialAnualForage2 = new PerennialAnualForage();
        perennialAnualForage2.setForage("Forage 2");
        perennialAnualForage2.setNote("Note 2");
        perennialAnualForage2.setProperty(convertToProperty(propertyController.findOne(1l)));
        
        PerennialAnualForage perennialAnualForage3 = new PerennialAnualForage();
        perennialAnualForage3.setForage("Forage 3");
        perennialAnualForage3.setNote("Note 3");
        perennialAnualForage3.setProperty(convertToProperty(propertyController.findOne(3l)));
        
        perennialAnualForageController.createRegister(perennialAnualForage1);
        perennialAnualForageController.createRegister(perennialAnualForage2);
        perennialAnualForageController.createRegister(perennialAnualForage3);
    }
	
	private void createForageDisponibility() {
		ForageDisponibility forageDisponibility1 = new ForageDisponibility();
		forageDisponibility1.setForage("forage 1");
		forageDisponibility1.setProperty(convertToProperty(propertyController.findOne(1l)));
		
		ForageDisponibility forageDisponibility2 = new ForageDisponibility();
		forageDisponibility2.setForage("forage 2");
		forageDisponibility2.setProperty(convertToProperty(propertyController.findOne(2l)));
		
		ForageDisponibility forageDisponibility3 = new ForageDisponibility();
		forageDisponibility3.setForage("forage 3");
		forageDisponibility3.setProperty(convertToProperty(propertyController.findOne(3l)));
		
		forageDisponibilityController.createRegister(forageDisponibility1);
		forageDisponibilityController.createRegister(forageDisponibility2);
		forageDisponibilityController.createRegister(forageDisponibility3);
	}
	
	private void createAnimals() {
        Animal animal1 = new Animal();
        animal1.setName("animal 1");
        animal1.setBreed("test 1");
        animal1.setProperty(convertToProperty(propertyController.findOne(3l)));
        
        Animal animal2 = new Animal();
        animal2.setName("animal 2");
        animal2.setBreed("test 2");
        animal2.setProperty(convertToProperty(propertyController.findOne(2l)));
        
        Animal animal3 = new Animal();
        animal3.setName("animal 3");
        animal3.setBreed("test 3");
        animal3.setProperty(convertToProperty(propertyController.findOne(1l)));
        
        animalController.createRegister(animal1);
        animalController.createRegister(animal2);
        animalController.createRegister(animal3);
    }
	
	private Region convertResponseEntityToRegion(ResponseEntity<Region> responseEntity) {
		Region reg = responseEntity.getBody();
		return reg;
	}
	
	private Property convertToProperty(ResponseEntity<PropertyDto> responseEntity) {
		PropertyDto dto = responseEntity.getBody();
		Property prop = dto.toProperty();
		return prop;
	}
	
	private User convertDtoToUser(ResponseEntity<UserDto> responseEntity) {
		UserDto dto = responseEntity.getBody();
		User user = dto.toUser();
    	return user;
    }
	
	private Vegetable convertDtoToVegetable(ResponseEntity<VegetableDto> responseEntity) {
		VegetableDto dto = responseEntity.getBody();
		Vegetable veg = dto.toVegetable();
		return veg;
	}
}
