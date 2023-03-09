package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.UserDto;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController extends CrudController<User, UserDto, Long>{
    private final UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        super(User.class,UserDto.class);
    	this.userService = userService;
        this.modelMapper = modelMapper;
    }

	@Override
	protected CrudService<User, Long> getService() {
		return this.userService;
	}

	@Override
	protected ModelMapper getModelMapper() {
		return this.modelMapper;
	}
	
	@GetMapping("/findName/{username}")
	public ResponseEntity<UserDto> findByName(@PathVariable String username){
		User entity = userService.findByName(username);
		
		if(entity != null) {
    		return ResponseEntity.ok(convertToDto(userService.findByName(username)));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
	}
	
	private UserDto convertToDto(User user) {
    	return modelMapper.map(user, UserDto.class);
    }
}
