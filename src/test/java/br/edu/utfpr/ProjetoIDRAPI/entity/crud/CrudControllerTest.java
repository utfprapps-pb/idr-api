package br.edu.utfpr.ProjetoIDRAPI.entity.crud;

import br.edu.utfpr.ProjetoIDRAPI.ApplicationTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;
import br.edu.utfpr.ProjetoIDRAPI.utils.EntityUtils;
import jakarta.validation.constraints.NotNull;
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

    static Object id;

    public static abstract class Dependent<T, D, ID extends Serializable> extends CrudControllerTest<T, D, ID> {
        public <DT> Object createValidDependency(Object dependentObject, String dependencyURL) {
            DT entity = (DT) dependentObject;

            ResponseEntity<Object> response = testRestTemplate.postForEntity(dependencyURL, entity, Object.class);
            Object dependencyId = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(dependencyId).isNotNull();

            EntityUtils.setIdValue(entity, dependencyId);

            return entity;
        }
    }

    protected abstract T createValidObject();
    protected abstract T createInvalidObject();
    protected abstract ID getValidId();
    protected abstract String getURL();

    @Test
    @Order(1)
    protected void createAndUpdateValidRegister() {
        T entity = createValidObject();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(getURL(), entity, Object.class);
        id = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        EntityUtils.setIdValue(entity, id);
        HttpEntity<T> requestEntity = new HttpEntity<>(entity, headers);
        response = testRestTemplate.exchange(getURL() + "/" + id, HttpMethod.PUT, requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(id).isNotNull();
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
    protected void findOneValid() {
        ResponseEntity<Object> response = testRestTemplate.getForEntity(getURL() + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(4)
    protected void findOneNonExistent() {
        ResponseEntity<Object> response = testRestTemplate.getForEntity(getURL() + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(5)
    protected void listAllRegisters() {
        ResponseEntity<List<D>> response = testRestTemplate.exchange(getURL(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(6)
    protected void deleteValidRegister() {
        ResponseEntity<Void> response = testRestTemplate.exchange(getURL() + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}