package br.gov.pr.idr.application.livestock.breed;

import br.gov.pr.idr.application.livestock.breed.update.UpdateBreedCommand;
import br.gov.pr.idr.application.livestock.breed.update.UpdateBreedUseCase;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
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
@DisplayName("UpdateBreedUseCase")
class UpdateBreedUseCaseTest {

    @Mock BreedGateway gateway;
    @InjectMocks UpdateBreedUseCase useCase;

    @Test
    void shouldThrowWhenUpdatingNonExistentBreed() {
        when(gateway.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new UpdateBreedCommand(UUID.randomUUID(), "x")));
    }
}
