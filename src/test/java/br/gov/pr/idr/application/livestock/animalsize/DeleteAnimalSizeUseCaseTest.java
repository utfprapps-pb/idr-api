package br.gov.pr.idr.application.livestock.animalsize;

import br.gov.pr.idr.application.livestock.animalsize.delete.DeleteAnimalSizeUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteAnimalSizeUseCase")
class DeleteAnimalSizeUseCaseTest {

    @Mock AnimalSizeGateway gateway;
    @Mock AnimalGateway animalGateway;
    @InjectMocks DeleteAnimalSizeUseCase useCase;

    @Test
    @DisplayName("deve deletar porte existente")
    void shouldDeleteExistingAnimalSize() {
        when(gateway.existsById(any())).thenReturn(true);
        when(animalGateway.existsBySizeId(any())).thenReturn(false);

        useCase.execute(UUID.randomUUID());

        verify(gateway).deleteById(any());
    }

    @Test
    @DisplayName("deve recusar excluir porte usado por algum animal")
    void shouldRefuseDeletingSizeInUse() {
        final var id = UUID.randomUUID();
        when(gateway.existsById(AnimalSizeID.from(id))).thenReturn(true);
        when(animalGateway.existsBySizeId(AnimalSizeID.from(id))).thenReturn(true);

        final var ex = assertThrows(UnprocessableEntityException.class, () -> useCase.execute(id));

        assertEquals("Porte em uso por animais cadastrados não pode ser excluído", ex.getContext());
        verify(gateway, never()).deleteById(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistentAnimalSize() {
        when(gateway.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
        verify(gateway, never()).deleteById(any());
        verifyNoInteractions(animalGateway);
    }
}
