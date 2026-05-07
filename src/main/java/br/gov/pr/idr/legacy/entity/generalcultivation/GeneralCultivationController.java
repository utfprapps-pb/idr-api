package br.gov.pr.idr.legacy.entity.generalcultivation;

import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.entity.generalcultivation.dto.GeneralCultivationDto;
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
