package br.gov.pr.idr.legacy.entity.user;

import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.entity.user.dto.UserDto;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("users")
public class UserController extends CrudController<User, UserDto, Long> {

    private final UserService userService;
    private final ModelMapper modelMapper;

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


}
