package br.edu.utfpr.ProjetoIDRAPI.entity.property.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;

import java.io.IOException;
import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyService;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertymap.PropertyImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PropertyServiceImpl extends CrudServiceImpl<Property, Long> implements PropertyService {

	private final PropertyRepository propertyRepository;
	
	public PropertyServiceImpl(PropertyRepository propertyRepository) {
		this.propertyRepository = propertyRepository;
	}

	@Override
	protected JpaRepository<Property, Long> getRepository() {
		return this.propertyRepository;
	}

	@Override
	public List<Property> findByUserId(Long id) {
		return propertyRepository.findAllByUserId(id);
	}

	@Override
	public JpaSpecificationExecutor<Property> getSpecExecutor() {
		return this.propertyRepository;
	}

    @Override
    @Transactional
    public Property findOne(Long aLong) {
        return super.findOne(aLong);
    }

    public Property save(Property entity, List<MultipartFile> files) {
        List<PropertyImage> images = mapFiles(files);
        if (images != null) {
            images.forEach(img -> img.setProperty(entity));
            entity.setImages(images);
        }
        return super.save(entity);
    }

    private List<PropertyImage> mapFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return null;
        return files.stream()
                .map(this::mapFileToImage)
                .toList();
    }

    private PropertyImage mapFileToImage(MultipartFile file) {
        try {
            return PropertyImage.builder().soilMap(file.getBytes()).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
