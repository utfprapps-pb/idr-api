package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.UserDto;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.service.UserService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
    public GenericResponse createUser(@RequestBody @Valid User user) {
        userService.save(user);
        return new GenericResponse("Usuário inserido com sucesso");
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findOneUser(@PathVariable Long id){
    	User user = userService.findOne(id);
    	
    	if(user != null) {
    		return ResponseEntity.ok(convertToDto(userService.findOne(id)));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
    }
    
    @GetMapping("/listUsers")
    public ResponseEntity<List<UserDto>> listAllRegisters(){
    	return ResponseEntity.ok(userService.findAll().stream()
    			.map(this::convertToDto)
    			.collect(Collectors.toList()));
    }
    
    @PutMapping("/{id}")
    public GenericResponse updateAResgister(@RequestBody @Valid User user) {
    	userService.save(user);
    	return new GenericResponse("Usuário foi atualizado!");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<User> removeOneRegister(@PathVariable Long id){
    	userService.delete(id);
    	return ResponseEntity.noContent().build();
    }
    
    private UserDto convertToDto(User user) {
    	return modelMapper.map(user, UserDto.class);
    }
    
}
