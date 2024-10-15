package br.edu.utfpr.ProjetoIDRAPI.entity.crud;

import br.edu.utfpr.ProjetoIDRAPI.ApplicationTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class CrudControllerTest<T, D, ID extends Serializable> {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    protected void createValidRegister() {
        T entity = createValidObject();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(getURL(), entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(2)
    protected void createInvalidRegister() {
        T entity = createInvalidObject();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(getURL(), entity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(3)
    protected void updateValidRegister() {
        ID id = getValidId();
        T entity = createValidObject();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<T> requestEntity = new HttpEntity<>(entity, headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(getURL() + "/" + id, HttpMethod.PUT, requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(4)
    protected void findOneValid() {
        ID id = getValidId();

        ResponseEntity<Object> response = testRestTemplate.getForEntity(getURL() + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(5)
    protected void findOneNonExistent() {
        ID id = getValidId();

        ResponseEntity<Object> response = testRestTemplate.getForEntity(getURL() + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(6)
    protected void listAllRegisters() {
        ResponseEntity<List<D>> response = testRestTemplate.exchange(getURL(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    protected abstract T createValidObject();
    protected abstract T createInvalidObject();
    protected abstract ID getValidId();
    protected abstract String getURL();
}