package br.edu.utfpr.ProjetoIDRAPI.entity.user;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.dto.UserDto;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController extends CrudController<User, UserDto, Long> {

    private final UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
		super(User.class, UserDto.class);
    	this.userService = userService;
        this.modelMapper = modelMapper;
    }

	@Override
	protected CrudService<User, Long> getService() {
		return this.userService;
	}

	@Override
	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	@GetMapping("/findUser/{username}")
	public ResponseEntity<UserDto> findByName(@PathVariable String username){
		User entity = userService.findByName(username);
		
		if(entity != null) {
    		return ResponseEntity.ok(convertToDto(userService.findByName(username)));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
	}

	@GetMapping("me")
	public ResponseEntity<UserDto> findOne() {
		User user = userService.findSelfUser();
		if (user != null) {
			return ResponseEntity.ok(convertToDto(user));
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@Deprecated(forRemoval = true)
	@GetMapping("/findSelfUser")
	public ResponseEntity<UserDto> findSelfUser(){
		return ResponseEntity.ok(convertToDto(userService.findSelfUser()));
	}
	
}
