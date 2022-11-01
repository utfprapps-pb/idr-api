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
    public GenericResponse createRegister(@RequestBody @Valid User user) {
        userService.save(user);
        return new GenericResponse("Registro inserido com sucesso");
    }
    
    @GetMapping("{id}")
    public ResponseEntity<UserDto> findOne(@PathVariable Long id){
    	User user = userService.findOne(id);
    	
    	if(user != null) {
    		return ResponseEntity.ok(convertToDto(userService.findOne(id)));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
    }
    
    @GetMapping
    public ResponseEntity<List<UserDto>> listAll(){
    	return ResponseEntity.ok(userService.findAll().stream()
    			.map(this::convertToDto)
    			.collect(Collectors.toList()));
    }
    
    @PutMapping("{id}")
    public GenericResponse updateResgister(@RequestBody @Valid User user) {
    	userService.save(user);
    	return new GenericResponse("Registro atualizado com sucesso");
    }
    
    @DeleteMapping("{id}")
    public GenericResponse deleteRegister(@PathVariable Long id){
    	userService.delete(id);
        return new GenericResponse("Registro excluido com sucesso");
    }
    
    private UserDto convertToDto(User user) {
    	return modelMapper.map(user, UserDto.class);
    }
}
