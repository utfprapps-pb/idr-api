package br.edu.utfpr.ProjetoIDRAPI.entity.user;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.dto.UserCreateDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.dto.UserDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
    	this.userService = userService;
        this.modelMapper = modelMapper;
    }

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid UserCreateDto user) {
		User userEntity = modelMapper.map(user, User.class);

		userService.save(userEntity);
		return new GenericResponse("Registro inserido com sucesso");
	}
	
	@PutMapping("{id}")
	public GenericResponse updateRegister(@RequestBody @Valid UserCreateDto user, @PathVariable Long id) {
		try {
			User ent = userService.findOne(id);
	    	
	    	if(ent != null) {
	    		User userEntity = modelMapper.map(user, User.class);
	    		
	    		userService.save(userEntity);
	    		return new GenericResponse("Registro atualizado com sucesso");
	    	}else {
	    		return new GenericResponse("Registro inexistente");
	    	}
		} catch (Exception e) {
			return new GenericResponse("Erro ao atualizar o registro!");
		}
    }
	
	@GetMapping
    public ResponseEntity<List<UserDto>> listAll(){
    	return ResponseEntity.ok(userService.findAll().stream()
    			.map(this::convertToDto)
    			.collect(Collectors.toList()));
    }
	
	@GetMapping("{id}")
    public ResponseEntity<UserDto> findOne(@PathVariable Long id){
    	User entity = userService.findOne(id);
    	
    	if(entity != null) {
    		return ResponseEntity.ok(convertToDto(userService.findOne(id)));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
    }
	
	@DeleteMapping("{id}")
	public ResponseEntity deleteRegister(@PathVariable Long id){
		try {
			userService.delete(id);
	    	return ResponseEntity.ok(new GenericResponse("Registro excluido com sucesso"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new GenericResponse("Não foi possível excluir o registro!"));
		}
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

	@GetMapping("/findSelfUser")
	public ResponseEntity<UserDto> findSelfUser(){
		return ResponseEntity.ok(convertToDto(userService.findSelfUser()));
	}
	
	private UserDto convertToDto(User user) {
    	return modelMapper.map(user, UserDto.class);
    }
}
