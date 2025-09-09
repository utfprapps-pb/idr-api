package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import java.util.Set;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.dto.UserDto;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;

public class UserControllerTest extends CrudControllerTest<User, UserDto, Long> {

    @Override
    protected User createValidObject() {
        return User.builder()
                .displayName("name")
                .username("username")
                .password("password")
                .cpf("09876543210")
                .userPermissions(Set.of())
                .build();
    }

    @Override
    protected User createInvalidObject() {
        return User.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/users";
    }

}