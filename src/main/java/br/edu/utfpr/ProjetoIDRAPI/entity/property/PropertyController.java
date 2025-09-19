package br.edu.utfpr.ProjetoIDRAPI.entity.property;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachment;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachmentDTO;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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
        return this.modelMapper;
    }

    @GetMapping("/userProperty/{id}")
    public ResponseEntity<List<PropertyDto>> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.findByUserId(id).stream().map(super::convertToDto).collect(Collectors.toList()));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<PropertyDto> findOne(Long id) {
        Property property = propertyService.findOne(id);
        if (property == null) {
            return ResponseEntity.noContent().build();
        }

        PropertyAttachmentDTO attachments = getAttachment(property.getId());
        PropertyDto propertyDTO = this.convertToDto(property);
        propertyDTO.setAttachment(attachments);
        return ResponseEntity.ok(propertyDTO);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> create(@RequestPart("property") @Valid PropertyDto dto, @RequestPart("attachment") MultipartFile attachment) throws IOException {
        Long id = save(dto, attachment);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyDto> update(@RequestPart("property") @Valid PropertyDto dto, @RequestPart("attachment") MultipartFile attachment, @PathVariable Long id) throws IOException {
        save(dto, attachment);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Long save(PropertyDto dto, MultipartFile attachment) throws IOException {
        Property entity = getModelMapper().map(dto, Property.class);
        if (attachment != null) {
            propertyService.save(entity, attachment.getBytes());
        } else {
            propertyService.save(entity);
        }
        return entity.getId();
    }

    public PropertyAttachmentDTO mapAttachment(PropertyAttachment attachment) {
        return getModelMapper().map(attachment, PropertyAttachmentDTO.class);
    }

    private PropertyAttachmentDTO getAttachment(Long id) {
        List<PropertyAttachmentDTO> attachments = getAttachments(id);
        return Optional.of(attachments).filter(Predicate.not(List::isEmpty))
                .map(l -> l.get(0)).orElse(null);
    }

    private List<PropertyAttachmentDTO> getAttachments(Long id) {
        return propertyService.findAttachmentsById(id).stream().map(this::mapAttachment).toList();
    }

}
