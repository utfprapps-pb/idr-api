package br.edu.utfpr.ProjetoIDRAPI.entity.crud;

import br.edu.utfpr.ProjetoIDRAPI.ApplicationTest;
import br.edu.utfpr.ProjetoIDRAPI.utils.EntityUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class CrudDependentControllerTest<DT, T, D, ID extends Serializable> extends CrudControllerTest<T, D, ID>{

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private static Object dependencyId;

    @Test
    @Order(1)
    @Override
    protected void createAndUpdateValidRegister() {
        DT dependencyEntity = createValidDependencyObject();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(getDependencyURL(), dependencyEntity, Object.class);
        dependencyId = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        System.out.println("1 ----");
        System.out.println(dependencyId);
        System.out.println(dependencyEntity);
        EntityUtils.setIdValue(dependencyEntity, dependencyId);
        System.out.println(dependencyEntity);

        assertThat(dependencyId).isNotNull();

        T entity = createValidObject(dependencyEntity);

        response = testRestTemplate.postForEntity(getURL(), entity, Object.class);
        id = response.getBody();

        System.out.println("2 ----");
        System.out.println(id);
        System.out.println(entity);
        EntityUtils.setIdValue(entity, id);
        System.out.println(entity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        EntityUtils.setIdValue(entity, id);
        HttpEntity<T> requestEntity = new HttpEntity<>(entity, headers);
        response = testRestTemplate.exchange(getURL() + "/" + id, HttpMethod.PUT, requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(id).isNotNull();
    }

    protected abstract String getDependencyURL();
    protected abstract DT createValidDependencyObject();
    protected abstract T createValidObject(DT property);
}
