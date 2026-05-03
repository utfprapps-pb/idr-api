package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.dto.GeneralCultivationDto;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("general-cultivations")
public class GeneralCultivationController extends CrudController<GeneralCultivation, GeneralCultivationDto, Long> {


    private final GeneralCultivationService service;
    private final ModelMapper mapper;

    public GeneralCultivationController(GeneralCultivationService service,
                                        ModelMapper mapper
    ) {
        super(GeneralCultivation.class, GeneralCultivationDto.class);
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    protected CrudService<GeneralCultivation, Long> getService() {
        return this.service;
    }


    @Override
    protected ModelMapper getModelMapper() {
        return this.mapper;
    }
}
