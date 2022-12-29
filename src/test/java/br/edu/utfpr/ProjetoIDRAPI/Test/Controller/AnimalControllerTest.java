package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AnimalControllerTest {
    private static final String API = "/animals";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AnimalRepository animalRepository;

    @BeforeEach()
    private void cleanup() {
        animalRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

}
