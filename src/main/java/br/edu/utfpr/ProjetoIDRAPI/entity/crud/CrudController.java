package br.edu.utfpr.ProjetoIDRAPI.entity.crud;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.utfpr.ProjetoIDRAPI.search.request.SearchRequest;
import br.edu.utfpr.ProjetoIDRAPI.utils.EntityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

public abstract class CrudController<T, D, ID extends Serializable> {

	protected abstract CrudService<T, ID> getService();
	protected abstract ModelMapper getModelMapper();
	private final Class<T> typeClass;
	private final Class<D> typeDtoClass;
	
	public CrudController(Class<T> typeClass, Class<D> typeDtoClass) {
		this.typeClass = typeClass;
		this.typeDtoClass = typeDtoClass;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ID> create(@RequestBody @Valid D dto) {
		T entity = getModelMapper().map(dto, this.typeClass);
		getService().save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(getId(entity));
	}

	@PutMapping("{id}")
	public ResponseEntity<D> update(@RequestBody @Valid D dto, @PathVariable ID id) {
		T entity = getModelMapper().map(dto, this.typeClass);
		getService().save(entity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

    public ResponseEntity<List<D>> findAll(){
    	return ResponseEntity.ok(getService().findAll().stream()
    			.map(this::convertToDto)
    			.collect(Collectors.toList()));
    }
	
	@GetMapping("{id}")
    public ResponseEntity<D> findOne(@PathVariable ID id){
    	T entity = getService().findOne(id);
    	
    	if(entity != null) {
    		return ResponseEntity.ok(convertToDto(entity));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
    }
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteRegister(@PathVariable ID id) {
		getService().delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/search")
	public ResponseEntity<Page<D>> search(@RequestBody @Valid SearchRequest entity){
		Page<D> page = getService().search(entity).map(this::convertToDto);
		return ResponseEntity.ok(page);
	}

	public D convertToDto(T entity) {
		return getModelMapper().map(entity, this.typeDtoClass);
	}

	public ID getId(T entity) {
		return EntityUtils.getIdValue(entity);
	}
}
