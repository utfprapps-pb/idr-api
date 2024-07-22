package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator.PropertyCollaboratorRepository;
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
public class PropertyCollaboratorControllerTest {
    private static final String API = "/propertyCollaborators";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PropertyCollaboratorRepository collaboratorRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    public void postCollaborator_whenCollaboratorIsValid_receiveCreated() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postCollaborator_whenCollaboratorIsValid_collaboratorSavedToDatabase() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        //Se compara com 4 pois existem três colaboradores padrões inseridos no banco.
        assertThat( collaboratorRepository.count() ).isEqualTo(4);
    }

    @Test
    public void deleteCollaborator_whenCollaboratorIdExists_receiveOk() {
        Property property = propertyRepository.findById(1L).orElse(null);

        PropertyCollaborator collaborator = createValidCollaborator();
        collaborator.setProperty(property);
        ResponseEntity<Object> responseProductUse =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        testRestTemplate.delete(API + "/4");

        //Se compara com 3 pois existem três colaboradores padrões inseridos no banco.
        assertThat( collaboratorRepository.count() ).isEqualTo(3);
    }

    @Test
    public void postCollaborator_whenCollaboratorIsValidAndAlreadyExists_collaboratorUpdateDatabase() {
        PropertyCollaborator collaborator = collaboratorRepository.findById(1L).orElse(null);
        collaborator.setCollaboratorName("Updated Josias");

        ResponseEntity<Object> response =
                testRestTemplate.postForEntity(API, collaborator, Object.class);

        PropertyCollaborator changedCollaborator = collaboratorRepository.findById(1L).orElse(null);
        assertThat(changedCollaborator.getCollaboratorName()).isEqualTo("Updated Josias");
    }

    @Test
    public void getCollaborator_whenCollaboratorExists_collaboratorReturnFromDatabase() {
        PropertyCollaborator collaboratorDB = collaboratorRepository.findById(1L).orElse(null);

        List<PropertyCollaborator> collaboratorList = collaboratorRepository.findAll();
        PropertyCollaborator collaboratorDB1 = collaboratorList.get(0);

        assertThat(collaboratorDB).isEqualTo(collaboratorDB1);
    }

    private PropertyCollaborator createValidCollaborator() {
        PropertyCollaborator collaborator = new PropertyCollaborator();
        collaborator.setCollaboratorName("Josias");
        collaborator.setWorkHours(12);
        collaborator.setWorkDays(7);

        return collaborator;
    }
}