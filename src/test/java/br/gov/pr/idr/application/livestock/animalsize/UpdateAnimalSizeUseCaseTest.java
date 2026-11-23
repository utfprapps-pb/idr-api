package br.gov.pr.idr.application.livestock.animalsize;

import br.gov.pr.idr.application.livestock.animalsize.update.UpdateAnimalSizeCommand;
import br.gov.pr.idr.application.livestock.animalsize.update.UpdateAnimalSizeUseCase;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateAnimalSizeUseCase")
class UpdateAnimalSizeUseCaseTest {

    @Mock AnimalSizeGateway gateway;
    @InjectMocks UpdateAnimalSizeUseCase useCase;

    @Test
    void shouldThrowWhenUpdatingNonExistentAnimalSize() {
        when(gateway.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new UpdateAnimalSizeCommand(UUID.randomUUID(), "x")));
    }
}
