package br.edu.utfpr.ProjetoIDRAPI.entity.crud;

import br.edu.utfpr.ProjetoIDRAPI.ApplicationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.io.Serializable;

@ApplicationTest
public abstract class CrudControllerTest<T, D, ID extends Serializable> {

    @Autowired
    protected RestTestClient restTestClient;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @AfterEach
    protected void cleanUp() {
        cleanUpDatabase();
    }

    protected abstract String getURL();

    protected abstract ID persistAndReturnId(T entity);

    protected abstract void cleanUpDatabase();

    protected abstract T createValidObject();

    protected abstract T createInvalidObject();

    protected abstract Class<D> getDtoClass();


    @Test
    protected void createValidRegister() {
        T entity = createValidObject();

        restTestClient.post()
                .uri(getURL())
                .body(entity)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    protected void createInvalidRegister() {
        T entity = createInvalidObject();

        restTestClient.post()
                .uri(getURL())
                .body(entity)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody();
    }

    @Test
    protected void updateValidRegister() {
        // Setup isolado: insere diretamente antes de testar
        T baseEntity = createValidObject();
        ID savedId = persistAndReturnId(baseEntity);

        T updatePayload = createValidObject(); // Assumindo alteração de dados

        restTestClient.put()
                .uri(getURL() + "/" + savedId)
                .body(updatePayload)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    protected void findOneValid() {
        ID savedId = persistAndReturnId(createValidObject());

        restTestClient.get()
                .uri(getURL() + "/" + savedId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    protected void findOneNonExistent() {
        restTestClient.get()
                .uri(getURL() + "/" + -890)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    protected void deleteValidRegister() {
        ID savedId = persistAndReturnId(createValidObject());

        restTestClient.delete()
                .uri(getURL() + "/" + savedId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }
}