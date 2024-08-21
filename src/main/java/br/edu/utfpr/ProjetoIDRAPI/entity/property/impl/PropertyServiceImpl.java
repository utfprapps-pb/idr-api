package br.edu.utfpr.ProjetoIDRAPI.entity.property.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

	@Override
	public JpaSpecificationExecutor<Property> getSpecExecutor() {
		return this.propertyRepository;
	}
}
