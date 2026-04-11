package br.edu.utfpr.ProjetoIDRAPI.entity.feed;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.feed.dto.FeedDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

@RestController
@RequestMapping("feeds")
public class FeedController extends CrudController<Feed, FeedDto, Long> {
	private final FeedService feedService;
	private final ModelMapper modelMapper;
	
	public FeedController(FeedService feedService, ModelMapper modelMapper) {
		super(Feed.class, FeedDto.class);
		this.feedService = feedService;
		this.modelMapper = modelMapper;
	}

	@Override
	protected CrudService<Feed, Long> getService() {
		return this.feedService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{name}")
	public ResponseEntity<FeedDto> findByName(@PathVariable String name){
		Feed entity = feedService.findByName(name);
		
		if(entity != null) {
			return ResponseEntity.ok(super.convertToDto(feedService.findByName(name)));
		} else {
    		return ResponseEntity.noContent().build();
    	}
	}
}
