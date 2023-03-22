package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.ProductUseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductUseControllerTest {
    private static final String API = "/productsUse";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private ProductUseRepository productUseRepository;

    @BeforeEach()
    private void cleanup() {
        productUseRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postProductUse_whenProductUseIsValid_receiveCreated() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        ProductUse productUse = createValidProductUse();
        productUse.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, productUse, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postProductUse_whenProductUseIsValid_productUseSavedToDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        ProductUse productUse = createValidProductUse();
        productUse.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, productUse, Object.class);

        assertThat( productUseRepository.count() ).isEqualTo(1);
    }

    @Test
    public void deleteProductUse_whenProductUseIdExists_receiveOk() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        ProductUse productUse = createValidProductUse();
        productUse.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, productUse, Object.class);
        productUse.setId(1L);

        testRestTemplate.delete(API + "/1");

        assertThat( productUseRepository.count() ).isEqualTo(0);
    }

    @Test
    public void postProductUse_whenProductUseIsValidAndAlreadyExists_productUseUpdateDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        ProductUse productUse = createValidProductUse();
        productUse.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, productUse, Object.class);
        productUse.setId(1L);
        productUse.setUsedFor("Updated UsedFor details");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, productUse, Object.class);

        List<ProductUse> productUseList = productUseRepository.findAll();
        ProductUse productUseDB = productUseList.get(0);
        assertThat(productUseDB.getUsedFor()).isEqualTo("Updated UsedFor details");
    }

    @Test
    public void getProductUse_whenProductUseExists_productUseReturnFromDatabase() {
        User user = createValidUser();
        ResponseEntity<Object> responseUser =
                testRestTemplate.postForEntity("/users", user, Object.class);
        user.setId(1L);

        Property property = createValidProperty();
        property.setUser(user);
        ResponseEntity<Object> responseProperty =
                testRestTemplate.postForEntity("/properties", property, Object.class);
        property.setId(1L);

        ProductUse productUse = createValidProductUse();
        productUse.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, productUse, Object.class);
        productUse.setId(1L);

        ProductUse productUseDB = productUseRepository.findById(1l).orElse(null);

        List<ProductUse> productUseList = productUseRepository.findAll();
        ProductUse productUseDB1 = productUseList.get(0);

        assertThat(productUseDB).isEqualTo(productUseDB1);
    }

    private User createValidUser() {
        User user = new User();
        user.setUsername("User-test-1");
        user.setPassword("115.675.888-66");
        user.setPhone("4632232277");
        user.setProfessionalRegister("999101");

        return user;
    }

    public Property createValidProperty() {
        Property property = new Property();
        property.setUser(createValidUser());
        property.setLeased(true);

        return property;
    }

    private ProductUse createValidProductUse() {
        LocalDate date = LocalDate.parse("2022-06-23");
        ProductUse productUse = new ProductUse();
        productUse.setProperty(createValidProperty());
        productUse.setUsedFor("UsedFor details");
        productUse.setQuantity(20);
        productUse.setUseDate(date);

        return productUse;
    }

}