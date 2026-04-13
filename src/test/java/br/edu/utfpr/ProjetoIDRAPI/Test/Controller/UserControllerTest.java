package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.dto.UserDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UserControllerTest extends CrudControllerTest<User, UserDto, Long> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected Long persistAndReturnId(User entity) {
        return userRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        userRepository.deleteAll();
    }

    @Override
    protected User createValidObject() {
        return TestUtils.createValidUser("ControllerTest");
    }

    @Override
    protected User createInvalidObject() {
        return new User();
    }

    @Override
    protected String getURL() {
        return "/users";
    }

    @Override
    protected Class<UserDto> getDtoClass() {
        return UserDto.class;
    }
}