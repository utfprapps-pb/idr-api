package br.gov.pr.idr.application.livestock.breed;

import br.gov.pr.idr.application.livestock.breed.delete.DeleteBreedUseCase;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteBreedUseCase")
class DeleteBreedUseCaseTest {

    @Mock BreedGateway gateway;
    @InjectMocks DeleteBreedUseCase useCase;

    @Test
    @DisplayName("deve deletar raça existente")
    void shouldDeleteExistingBreed() {
        when(gateway.existsById(any())).thenReturn(true);

        useCase.execute(UUID.randomUUID());

        verify(gateway).deleteById(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistentBreed() {
        when(gateway.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
    }
}
