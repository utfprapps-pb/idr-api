package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class PropertyControllerTest extends CrudControllerTest<Property, PropertyDto, Long> {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    private User savedOwner;
    private User savedTechUser;

    @BeforeEach
    void setup() {
        savedOwner = TestUtils.createValidUser("Owner");
        userRepository.save(savedOwner);

        savedTechUser = TestUtils.createValidUser("Tech");
        userRepository.save(savedTechUser);
    }

    @Override
    protected Long persistAndReturnId(Property entity) {
        return propertyRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    protected Property createValidObject() {
        PropertyTechnician technician = TestUtils.createValidPropertyTechnician(savedTechUser);
        return TestUtils.createValidProperty(savedOwner, List.of(technician));
    }

    @Override
    protected Property createInvalidObject() {
        return new Property();
    }

    @Override
    protected String getURL() {
        return "/properties";
    }

    @Override
    protected Class<PropertyDto> getDtoClass() {
        return PropertyDto.class;
    }

    @Test
    protected void createValidRegisterWithAttachment() throws JsonProcessingException {
        Property entity = createValidObject();
        String json = toJSON(entity);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("property", new HttpEntity<>(json, createJsonHeaders()));
        body.add("attachment", createAttachment());

        restTestClient.post()
                .uri(getURL())
                .body(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$").isNotEmpty();
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String toJSON(Property property) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(property);
    }

    private ByteArrayResource createAttachment() {
        return new ByteArrayResource("conteúdo do arquivo".getBytes()) {
            @Override
            public String getFilename() {
                return "file1.txt";
            }
        };
    }
}