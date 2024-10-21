package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableplague.dto.VegetablePlagueDto;

public class VegetablePlagueControllerTest extends CrudControllerTest<VegetablePlague, VegetablePlagueDto, Long> {
	@Override
	protected VegetablePlague createValidObject() {
		return VegetablePlague.builder()
				.plague(Plague.builder().id(1L).build())
				.culture(Culture.builder().id(1L).build())
				.property(Property.builder().id(1L).build())
				.infestationType("Teste")
				.build();
	}

	@Override
	protected VegetablePlague createInvalidObject() {
		return VegetablePlague.builder().build();
	}

	@Override
	protected Long getValidId() {
		return 1L;
	}

	@Override
	protected String getURL() {
		return "/vegetableplagues";
	}
}