package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyService;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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

}
