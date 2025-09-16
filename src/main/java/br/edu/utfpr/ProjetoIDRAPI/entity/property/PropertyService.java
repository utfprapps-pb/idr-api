package br.edu.utfpr.ProjetoIDRAPI.entity.property;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import org.springframework.web.multipart.MultipartFile;

public interface PropertyService extends CrudService<Property, Long> {

	List<Property> findByUserId(Long id);

    Property save(Property entity, List<MultipartFile> files);
}
