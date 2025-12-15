package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyarea.PropertyArea;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyControllerTest extends CrudControllerTest<Property, PropertyDto, Long> {

    @Override
    protected Property createValidObject() {
        return Property.builder().ocupationArea("Occupation Area 1").totalArea(BigDecimal.valueOf(123.32)).latitude(BigInteger.valueOf(1365)).longitude(BigInteger.valueOf(1365)).leased(true).user(User.builder().id(1L).build()).name("Property 1").city("City 1").state("State 1").nakedAveragePrice(1000.50).leaseAveragePrice(1000.50).farmer("Farmer 1").collaborators(List.of(PropertyCollaborator.builder().collaboratorName("Collaborator 1").workHours(3).workDays(2).build())).area(PropertyArea.builder().dairyCattleFarming(132.32).perennialPasture(132.32).summerPlowing(132.32).winterPlowing(132.32).build()).technicians(List.of(PropertyTechnician.builder().user(User.builder().id(1L).build()).build())).build();
    }

    @Override
    protected Property createInvalidObject() {
        return Property.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/properties";
    }

    @Test
    @Order(20)
    protected void createValidRegisterWithAttachment() throws JsonProcessingException {
        Property entity = createValidObject();
        String json = toJSON(entity);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("property", new HttpEntity<>(json, createJsonHeaders()));
        body.add("attachment", createAttachment());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(getURL(), requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
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