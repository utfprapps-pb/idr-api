package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.feed.Feed;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.Pest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.CultivationPest;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.dto.CultivationPestDto;

public class VegetablePestControllerTest extends CrudControllerTest<CultivationPest, CultivationPestDto, Long> {
	@Override
	protected CultivationPest createValidObject() {
		return CultivationPest.builder()
				.pest(Pest.builder().id(1L).build())
				.feed(Feed.builder().id(1L).build())
				.property(Property.builder().id(1L).build())
				.infestationType("Teste")
				.build();
	}

	@Override
	protected CultivationPest createInvalidObject() {
		return CultivationPest.builder().build();
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