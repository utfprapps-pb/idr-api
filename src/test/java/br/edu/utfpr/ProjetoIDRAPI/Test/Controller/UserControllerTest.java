package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import java.util.Set;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.dto.UserDto;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Override
    protected void updateValidRegister() {
        User entity = createValidObject();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(entity, headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(getURL() + "/" + 999L, HttpMethod.PUT, requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}