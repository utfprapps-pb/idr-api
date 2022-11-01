package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl implements PropertyService {
	private final PropertyRepository propertyRepository;
	
	public PropertyServiceImpl(PropertyRepository propertyRepository) {
		this.propertyRepository = propertyRepository;
	}
	
	@Override
	public Property save(Property property) {
		return propertyRepository.save(property);
	}

	@Override
	public Property findOne(Long id) {
		return propertyRepository.findById(id).orElse(null);
	}

	@Override
	public List<Property> findAll() {
		return propertyRepository.findAll();
	}

	@Override
	public List<Property> findByUserId(Long id) {
		return propertyRepository.findAllByUserId(id);
	}

	@Override
	public void delete(Long id) {
		propertyRepository.deleteById(id);
	}
}
