package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.PlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague.VegetablePlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague.dto.VegetablePlagueDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VegetablePlagueControllerTest extends CrudControllerTest<VegetablePlague, VegetablePlagueDto, Long> {

	@Autowired
	private VegetablePlagueRepository vegetablePlagueRepository;
	@Autowired
	private PlagueRepository plagueRepository;
	@Autowired
	private CultureRepository cultureRepository;
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private UserRepository userRepository;

	private Property savedProperty;
	private Plague savedPlague;
	private Culture savedCulture;

	@BeforeEach
	void setup() {
		User owner = TestUtils.createValidUser("Owner");
		userRepository.save(owner);

		User techUser = TestUtils.createValidUser("Tech");
		userRepository.save(techUser);

		PropertyTechnician technician = TestUtils.createValidPropertyTechnician(techUser);
		savedProperty = TestUtils.createValidProperty(owner, List.of(technician));
		propertyRepository.save(savedProperty);

		savedPlague = TestUtils.createValidPlague();
		plagueRepository.save(savedPlague);

		savedCulture = TestUtils.createValidCulture();
		cultureRepository.save(savedCulture);
	}

	@Override
	protected Long persistAndReturnId(VegetablePlague entity) {
		return vegetablePlagueRepository.save(entity).getId();
	}

	@Override
	protected void cleanUpDatabase() {
		vegetablePlagueRepository.deleteAll();
		plagueRepository.deleteAll();
		cultureRepository.deleteAll();
		propertyRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Override
	protected VegetablePlague createValidObject() {
		return TestUtils.createValidVegetablePlague(savedPlague, savedCulture, savedProperty);
	}

	@Override
	protected VegetablePlague createInvalidObject() {
		return new VegetablePlague();
	}

	@Override
	protected String getURL() {
		return "/vegetableplagues";
	}

	@Override
	protected Class<VegetablePlagueDto> getDtoClass() {
		return VegetablePlagueDto.class;
	}
}