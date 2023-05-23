package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.VegetablePlagueDto;
import br.edu.utfpr.ProjetoIDRAPI.model.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetablePlagueService;

@RestController
@RequestMapping("vegetableplagues")
public class VegetablePlagueController extends CrudController<VegetablePlague, VegetablePlagueDto, Long> {

	private final VegetablePlagueService VegetablePlagueService;
	private ModelMapper modelMapper;
	
	public VegetablePlagueController(VegetablePlagueService VegetablePlagueService, ModelMapper modelMapper) {
		super(VegetablePlague.class,VegetablePlagueDto.class);
		this.VegetablePlagueService = VegetablePlagueService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<VegetablePlague, Long> getService() {
		return this.VegetablePlagueService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
}
