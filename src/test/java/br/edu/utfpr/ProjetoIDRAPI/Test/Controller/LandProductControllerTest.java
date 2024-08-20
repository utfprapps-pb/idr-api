package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.LandProduct;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.LandProductRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LandProductControllerTest {

    private static final String API = "/landProducts";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private LandProductRepository productRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    public void postLandProduct_whenLandProductIsValid_receiveCreated() {
        Property property = propertyRepository.findById(1L).orElse(null);

        LandProduct productUse = createValidLandProduct();
        productUse.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, productUse, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postLandProduct_whenLandProductIsValid_landProductSavedToDatabase() {
        Property property = propertyRepository.findById(1L).orElse(null);

        LandProduct productUse = createValidLandProduct();
        productUse.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, productUse, Object.class);

        //Se compara com 4 pois existem três produtos padrões inseridos no banco.
        assertThat( productRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteLandProduct_whenLandProductIdExists_receiveOk() {
        Property property = propertyRepository.findById(1L).orElse(null);

        LandProduct productUse = createValidLandProduct();
        productUse.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, productUse, Object.class);
        productUse.setId(1L);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três produtos padrões inseridos no banco.
        assertThat( productRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postProductUse_whenProductUseIsValidAndAlreadyExists_productUseUpdateDatabase() {
        LandProduct product = productRepository.findById(1L).orElse(null);
        product.setUsedFor("Changed UsedFor details");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, product, Object.class);

        LandProduct changedProduct = productRepository.findById(1L).orElse(null);
        assertThat(changedProduct.getUsedFor()).isEqualTo("Changed UsedFor details");
    }

    @Test
    public void getProductUse_whenProductUseExists_productUseReturnFromDatabase() {
        LandProduct productDB = productRepository.findById(1l).orElse(null);

        List<LandProduct> productList = productRepository.findAll();
        LandProduct productDB1 = productList.get(0);

        assertThat(productDB).isEqualTo(productDB1);
    }

    private LandProduct createValidLandProduct() {
        LandProduct productUse = new LandProduct();
        productUse.setUsedFor("UsedFor details");
        productUse.setQuantity(20);

        return productUse;
    }
}