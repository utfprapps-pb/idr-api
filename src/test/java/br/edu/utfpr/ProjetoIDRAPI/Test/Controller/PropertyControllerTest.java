package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PropertyControllerTest {
    private static final String API = "/properties";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PropertyRepository propertyRepository;

    @BeforeEach()
    private void cleanup() {
        propertyRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

}
