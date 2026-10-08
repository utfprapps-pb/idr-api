package br.gov.pr.idr.legacy.entity.animal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import br.gov.pr.idr.infra.shared.error.GlobalExceptionHandler;

class AnimalControllerMvcTest {

    private final AnimalService animalService = mock(AnimalService.class);
    private final MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new AnimalController(animalService, new ModelMapper()))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    @DisplayName("POST create monta a entidade com propertyId e bornDate corretos")
    void createBuildsEntityWithPropertyIdAndBornDate() throws Exception {
        UUID propertyId = UUID.randomUUID();

        mvc.perform(post("/properties/" + propertyId + "/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"propertyId":"%s","identifier":"Mimosa","breed":{"id":1},"bornDate":"2019-06-23"}
                                """.formatted(propertyId)));

        ArgumentCaptor<Animal> captor = ArgumentCaptor.forClass(Animal.class);
        verify(animalService).save(captor.capture());
        assertEquals(propertyId, captor.getValue().getPropertyId());
        assertEquals(LocalDate.of(2019, 6, 23), captor.getValue().getBornDate());
    }

    @Test
    @DisplayName("PUT update monta a entidade com propertyId e bornDate corretos")
    void updateBuildsEntityWithPropertyIdAndBornDate() throws Exception {
        UUID propertyId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        when(animalService.update(eq(id), any(Animal.class))).thenAnswer(invocation -> invocation.getArgument(1));

        mvc.perform(put("/properties/" + propertyId + "/animals/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"propertyId":"%s","identifier":"Charmosa","breed":{"id":2},"bornDate":"2023-06-23"}
                                """.formatted(propertyId)));

        ArgumentCaptor<Animal> captor = ArgumentCaptor.forClass(Animal.class);
        verify(animalService).update(eq(id), captor.capture());
        assertEquals(propertyId, captor.getValue().getPropertyId());
        assertEquals(LocalDate.of(2023, 6, 23), captor.getValue().getBornDate());
    }
}
