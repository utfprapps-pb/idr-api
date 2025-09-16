package br.edu.utfpr.ProjetoIDRAPI.entity.property;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("properties")
public class PropertyController extends CrudController<Property, PropertyDto, Long> {

	private final PropertyService propertyService;
	private ModelMapper modelMapper;
	
	public PropertyController(PropertyService propertyService, ModelMapper modelMapper) {
		super(Property.class, PropertyDto.class);
		this.propertyService = propertyService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	protected CrudService<Property, Long> getService() {
		return this.propertyService;
	}

	@Override
	protected ModelMapper getModelMapper() {
        modelMapper.getConfiguration().setCollectionsMergeEnabled(false);
		return this.modelMapper;
	}
	
	@GetMapping("/userProperty/{id}")
	public ResponseEntity<List<PropertyDto>> findByUserId(@PathVariable Long id) {
		return ResponseEntity.ok(
				propertyService.findByUserId(id)
						.stream()
						.map(super::convertToDto)
						.collect(Collectors.toList())
		);
	}

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> create(
            @RequestPart("property") @Valid Property entity,
            @RequestPart(value = "images", required = false) List<MultipartFile> files
    ) {
        propertyService.save(entity, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(getId(entity));
    }
}
