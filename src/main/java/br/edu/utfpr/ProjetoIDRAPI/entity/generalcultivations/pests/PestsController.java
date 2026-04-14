package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests.dto.PestsDto;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("general-cultivations/pests")
public class PestsController extends CrudController<Pests, PestsDto, Long> {

    private final PestsService pestsService;
    private final ModelMapper modelMapper;

    PestsController(PestsService pestsService,ModelMapper modelMapper) {
        super(Pests.class, PestsDto.class);
        this.pestsService = pestsService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<Pests, Long> getService() {
        return this.pestsService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

}
