package br.edu.utfpr.ProjetoIDRAPI.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.dto.ForageDisponibilityDto;
import br.edu.utfpr.ProjetoIDRAPI.model.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.ForageDisponibilityService;

@RestController
@RequestMapping("foragesDisponibilities")
public class ForageDisponibilityController extends CrudController<ForageDisponibility, ForageDisponibilityDto, Long>{
	private final ForageDisponibilityService forageService;
	private ModelMapper modelMapper;
	
	public ForageDisponibilityController(ForageDisponibilityService forageService, ModelMapper modelMapper) {
		super(ForageDisponibility.class, ForageDisponibilityDto.class);
		this.forageService = forageService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<ForageDisponibility, Long> getService() {
		return this.forageService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}

}
