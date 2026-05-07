package br.gov.pr.idr.legacy.entity.pest;

import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.entity.pest.dto.PestDto;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("general-cultivations/pests")
public class PestController extends CrudController<Pest, PestDto, Long> {

    private final PestService pestService;
    private final ModelMapper modelMapper;

    PestController(PestService pestService, ModelMapper modelMapper) {
        super(Pest.class, PestDto.class);
        this.pestService = pestService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<Pest, Long> getService() {
        return this.pestService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

}
