package br.gov.pr.idr.application.livestock.animalsize;

import br.gov.pr.idr.application.livestock.animalsize.delete.DeleteAnimalSizeUseCase;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
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
@DisplayName("DeleteAnimalSizeUseCase")
class DeleteAnimalSizeUseCaseTest {

    @Mock AnimalSizeGateway gateway;
    @InjectMocks DeleteAnimalSizeUseCase useCase;

    @Test
    @DisplayName("deve deletar porte existente")
    void shouldDeleteExistingAnimalSize() {
        when(gateway.existsById(any())).thenReturn(true);

        useCase.execute(UUID.randomUUID());

        verify(gateway).deleteById(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistentAnimalSize() {
        when(gateway.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
    }
}
