package br.edu.utfpr.ProjetoIDRAPI.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.edu.utfpr.ProjetoIDRAPI.controller.AnimalController;
import br.edu.utfpr.ProjetoIDRAPI.controller.BreedController;
import br.edu.utfpr.ProjetoIDRAPI.controller.CityController;
import br.edu.utfpr.ProjetoIDRAPI.controller.ForageDisponibilityController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PerennialAnualForageController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PlagueDiseaseController;
import br.edu.utfpr.ProjetoIDRAPI.controller.LandProductController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PropertyCollaboratorController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PropertyController;
import br.edu.utfpr.ProjetoIDRAPI.controller.PropertyEquipImproveController;
import br.edu.utfpr.ProjetoIDRAPI.controller.RegionController;
import br.edu.utfpr.ProjetoIDRAPI.controller.UserController;
import br.edu.utfpr.ProjetoIDRAPI.controller.VegetableController;
import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.dto.UserCreateDto;
import br.edu.utfpr.ProjetoIDRAPI.dto.VegetableDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.model.Breed;
import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.model.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.model.PlagueDisease;
import br.edu.utfpr.ProjetoIDRAPI.model.LandProduct;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.model.Region;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;
import br.edu.utfpr.ProjetoIDRAPI.repository.UserRepository;

//Classe responsavel por adicionar dados "fixos" ao banco de dados
@Component
public class DataLoader implements CommandLineRunner {
	@Autowired
	private UserController userController;
	@Autowired
	private UserRepository userRepository;
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
	private LandProductController productUseController;
	@Autowired
	private PlagueDiseaseController plagueDiseaseController;
	@Autowired
	private PerennialAnualForageController perennialAnualForageController;
	@Autowired
	private CityController cityController;
	@Autowired
	private BreedController breedController;

	private ModelMapper modelMapper;

	public DataLoader(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

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
		user1.setUsername("fulano1@test.com");
		user1.setDisplayName("Fulano 1");
		user1.setPassword("123");
		user1.setCpf("111.111.111-11");
		user1.setCity("Pato Branco");
		user1.setCep("1111.1111");
		user1.setStreet("Rua teste1");
		user1.setHouseNumber("01");
		user1.setPhone("1111");
		user1.setProfessionalRegister("1111");
		user1.setGraduationYear("1991");

		User user2 = new User();
		user2.setUsername("fulano2@test.com");
		user2.setDisplayName("Fulano 2");
		user2.setPassword("123");
		user2.setCpf("222.222.222-22");
		user2.setCity("Beltrao");
		user2.setCep("2222.2222");
		user2.setStreet("Rua teste2");
		user2.setHouseNumber("22");
		user2.setPhone("2222");
		user2.setProfessionalRegister("2222");
		user2.setGraduationYear("2002");

		User user3 = new User();
		user3.setUsername("fulano3@test.com");
		user3.setDisplayName("Fulano 3");
		user3.setPassword("123");
		user3.setCpf("333.333.333-33");
		user3.setCity("Palmas");
		user3.setCep("3333.3333");
		user3.setStreet("Rua teste3");
		user3.setHouseNumber("333");
		user3.setPhone("3333");
		user3.setProfessionalRegister("3333");
		user3.setGraduationYear("2013");

		UserCreateDto userEntity1 = modelMapper.map(user1, UserCreateDto.class);
		UserCreateDto userEntity2 = modelMapper.map(user2, UserCreateDto.class);
		UserCreateDto userEntity3 = modelMapper.map(user3, UserCreateDto.class);

		userController.createRegister(userEntity1);
		userController.createRegister(userEntity2);
		userController.createRegister(userEntity3);
	}

	private void createProperties() {
		BigDecimal b = new BigDecimal("163.46");
		BigInteger bi = new BigInteger("1365");

		Property property1 = new Property();
		property1.setUser(userRepository.findById(1L).get());
		property1.setOcupationArea("Ocupation Area1");
		property1.setTotalArea(b);
		property1.setLatitude(bi);
		property1.setLongitude(bi);
		property1.setLeased(true);

		b = new BigDecimal("634.13");
		bi = new BigInteger("654");

		Property property2 = new Property();
		property2.setUser(userRepository.findById(1L).get());
		property2.setOcupationArea("Ocupation Area2");
		property2.setTotalArea(b);
		property2.setLatitude(bi);
		property2.setLongitude(bi);
		property2.setLeased(true);

		b = new BigDecimal("389.16");
		bi = new BigInteger("365");

		Property property3 = new Property();
		property3.setUser(userRepository.findById(2L).get());
		property3.setOcupationArea("Ocupation Area3");
		property3.setTotalArea(b);
		property3.setLatitude(bi);
		property3.setLongitude(bi);
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
		Vegetable vegetable1 = new Vegetable(1l, convertToProperty(propertyController.findOne(2l)), "Vegetable 1");
		Vegetable vegetable2 = new Vegetable(2l, convertToProperty(propertyController.findOne(2l)), "Vegetable 2");
		Vegetable vegetable3 = new Vegetable(3l, convertToProperty(propertyController.findOne(3l)), "Vegetable 3");

		vegetableController.createRegister(vegetable1);
		vegetableController.createRegister(vegetable2);
		vegetableController.createRegister(vegetable3);
	}

	private void createPropertyEquipImprove() {
		BigDecimal u = new BigDecimal("135.13");
		BigDecimal p = new BigDecimal("965.36");
		BigDecimal v = new BigDecimal("136.16");

		PropertyEquipImprove equipImprove1 = new PropertyEquipImprove();
		equipImprove1.setType("Test type");
		equipImprove1.setName("Equipament 1");
		equipImprove1.setQuantity(10);
		equipImprove1.setUnityValue(u);
		equipImprove1.setPercentageCattle(p);
		equipImprove1.setUtilLife(16);
		equipImprove1.setAquisitionDate(new Date());
		equipImprove1.setValueCattle(v);
		equipImprove1.setProperty(convertToProperty(propertyController.findOne(3l)));

		PropertyEquipImprove equipImprove2 = new PropertyEquipImprove();
		equipImprove2.setType("Test type");
		equipImprove2.setName("Equipament 2");
		equipImprove2.setQuantity(22);
		equipImprove2.setUnityValue(u);
		equipImprove2.setPercentageCattle(p);
		equipImprove2.setUtilLife(36);
		equipImprove2.setAquisitionDate(new Date());
		equipImprove2.setValueCattle(v);
		equipImprove2.setProperty(convertToProperty(propertyController.findOne(1l)));

		PropertyEquipImprove equipImprove3 = new PropertyEquipImprove();
		equipImprove3.setType("Test type");
		equipImprove3.setName("Equipament 3");
		equipImprove3.setQuantity(3);
		equipImprove3.setUnityValue(u);
		equipImprove3.setPercentageCattle(p);
		equipImprove3.setUtilLife(15);
		equipImprove3.setAquisitionDate(new Date());
		equipImprove3.setValueCattle(v);
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
		LandProduct productUse1 = new LandProduct();
		productUse1.setProperty(convertToProperty(propertyController.findOne(1l)));
		productUse1.setUsedFor("UsedFor details 1");
		productUse1.setQuantity(20);
		productUse1.setUseDate(date);

		LandProduct productUse2 = new LandProduct();
		productUse2.setProperty(convertToProperty(propertyController.findOne(3l)));
		productUse2.setUsedFor("UsedFor details 2");
		productUse2.setQuantity(20);
		productUse2.setUseDate(date);

		LandProduct productUse3 = new LandProduct();
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
		plagueDisease1.setCulture("Culture 1");
		plagueDisease1.setIdentification("ident 1");
		plagueDisease1.setType("Type 1");

		PlagueDisease plagueDisease2 = new PlagueDisease();
		plagueDisease2.setIdVegetable(convertDtoToVegetable(vegetableController.findByName("Vegetable 2")));
		plagueDisease2.setCulture("Culture 2");
		plagueDisease2.setIdentification("ident 2");
		plagueDisease2.setType("Type 2");

		PlagueDisease plagueDisease3 = new PlagueDisease();
		plagueDisease3.setIdVegetable(convertDtoToVegetable(vegetableController.findByName("Vegetable 3")));
		plagueDisease3.setCulture("Culture 3");
		plagueDisease3.setIdentification("ident 3");
		plagueDisease3.setType("Type 3");

		plagueDiseaseController.createRegister(plagueDisease1);
		plagueDiseaseController.createRegister(plagueDisease2);
		plagueDiseaseController.createRegister(plagueDisease3);
	}

	private void createForages() {
		LocalDate date = LocalDate.parse("2022-06-23");
		
		PerennialAnualForage perennialAnualForage1 = new PerennialAnualForage();
		perennialAnualForage1.setType("Type 1");
		perennialAnualForage1.setPrice(16.12);
		perennialAnualForage1.setForage("Forage 1");
		perennialAnualForage1.setArea(165.13);
		perennialAnualForage1.setAverageCost(136.45);
		perennialAnualForage1.setUtilLife(13);
		perennialAnualForage1.setFormationDate(date);
		perennialAnualForage1.setNote("Note 1");
		perennialAnualForage1.setProperty(convertToProperty(propertyController.findOne(2l)));

		PerennialAnualForage perennialAnualForage2 = new PerennialAnualForage();
		perennialAnualForage2.setType("Type 2");
		perennialAnualForage2.setPrice(35.16);
		perennialAnualForage2.setForage("Forage 2");
		perennialAnualForage2.setArea(365.46);
		perennialAnualForage2.setAverageCost(63.45);
		perennialAnualForage2.setUtilLife(16);
		perennialAnualForage2.setFormationDate(date);
		perennialAnualForage2.setNote("Note 2");
		perennialAnualForage2.setProperty(convertToProperty(propertyController.findOne(1l)));

		PerennialAnualForage perennialAnualForage3 = new PerennialAnualForage();
		perennialAnualForage3.setType("Type 3");
		perennialAnualForage3.setPrice(51.12);
		perennialAnualForage3.setForage("Forage 3");
		perennialAnualForage3.setArea(65.13);
		perennialAnualForage3.setAverageCost(365.45);
		perennialAnualForage3.setUtilLife(15);
		perennialAnualForage3.setFormationDate(date);
		perennialAnualForage3.setNote("Note 3");
		perennialAnualForage3.setProperty(convertToProperty(propertyController.findOne(3l)));

		perennialAnualForageController.createRegister(perennialAnualForage1);
		perennialAnualForageController.createRegister(perennialAnualForage2);
		perennialAnualForageController.createRegister(perennialAnualForage3);
	}

	private void createForageDisponibility() {
		LocalDate date = LocalDate.parse("2022-06-23");
		BigInteger bi = new BigInteger("1365");
		
		ForageDisponibility forageDisponibility1 = new ForageDisponibility();
		forageDisponibility1.setDate(date);
		forageDisponibility1.setForage("forage 1");
		forageDisponibility1.setEntry(15.1f);
		forageDisponibility1.setResidue(16f);
		forageDisponibility1.setKg(16f);
		forageDisponibility1.setPicketArea(169f);
		forageDisponibility1.setEfficiency(129f);
		forageDisponibility1.setNumCows(bi);
		forageDisponibility1.setKgCows(36f);
		forageDisponibility1.setProperty(convertToProperty(propertyController.findOne(1l)));

		ForageDisponibility forageDisponibility2 = new ForageDisponibility();
		forageDisponibility2.setDate(date);
		forageDisponibility2.setForage("forage 2");
		forageDisponibility2.setEntry(64.5f);
		forageDisponibility2.setResidue(36f);
		forageDisponibility2.setKg(46f);
		forageDisponibility2.setPicketArea(26f);
		forageDisponibility2.setEfficiency(465f);
		forageDisponibility2.setNumCows(bi);
		forageDisponibility2.setKgCows(46f);
		forageDisponibility2.setProperty(convertToProperty(propertyController.findOne(2l)));

		ForageDisponibility forageDisponibility3 = new ForageDisponibility();
		forageDisponibility3.setDate(date);
		forageDisponibility3.setForage("forage 3");
		forageDisponibility3.setEntry(46.9f);
		forageDisponibility3.setResidue(36f);
		forageDisponibility3.setKg(45f);
		forageDisponibility3.setPicketArea(265f);
		forageDisponibility3.setEfficiency(121f);
		forageDisponibility3.setNumCows(bi);
		forageDisponibility3.setKgCows(32f);
		forageDisponibility3.setProperty(convertToProperty(propertyController.findOne(3l)));

		forageDisponibilityController.createRegister(forageDisponibility1);
		forageDisponibilityController.createRegister(forageDisponibility2);
		forageDisponibilityController.createRegister(forageDisponibility3);
	}

	private void createAnimals() {
		LocalDate date = LocalDate.parse("2021-06-23");
		
		Breed breed1 = new Breed();
		breed1.setBreedName("Holandês");
		breedController.createRegister(breed1);

		Breed breed2 = new Breed();
		breed2.setBreedName("Girolando");
		breedController.createRegister(breed2);

		Animal animal1 = new Animal();
		animal1.setType("type 1");
		animal1.setIdentifier("identifier 1");
		animal1.setGender("M");
		animal1.setBornCondition("Vivo");
		animal1.setBreed(breed1);
		animal1.setBornDate(date);
		animal1.setBornWeight(12f);
		animal1.setPreviousWeight(45f);
		animal1.setCurrentWeight(62f);
		animal1.setEcc(32f);
		animal1.setProperty(convertToProperty(propertyController.findOne(3l)));

		Animal animal2 = new Animal();
		animal2.setAnimalMother(animal1);
		animal2.setType("type 2");
		animal2.setIdentifier("identifier 2");
		animal2.setGender("F");
		animal2.setBornCondition("Morto");
		animal2.setBreed(breed2);
		animal2.setBornDate(LocalDate.now());
		animal2.setBornWeight(10f);
		animal2.setPreviousWeight(36f);
		animal2.setCurrentWeight(54f);
		animal2.setEcc(36f);
		animal2.setProperty(convertToProperty(propertyController.findOne(2l)));

		Animal animal3 = new Animal();
		animal3.setAnimalMother(animal1);
		animal3.setType("type 3");
		animal3.setIdentifier("identifier 3");
		animal3.setGender("F");
		animal3.setBornCondition("Vivo");
		animal3.setBreed(breed1);
		animal3.setBornDate(LocalDate.now());
		animal3.setBornWeight(16f);
		animal3.setPreviousWeight(26f);
		animal3.setCurrentWeight(62f);
		animal3.setEcc(36f);
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

	private Vegetable convertDtoToVegetable(ResponseEntity<VegetableDto> responseEntity) {
		VegetableDto dto = responseEntity.getBody();
		Vegetable veg = dto.toVegetable();
		return veg;
	}
}