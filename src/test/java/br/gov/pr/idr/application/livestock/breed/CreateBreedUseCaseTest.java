package br.gov.pr.idr.application.livestock.breed;

import br.gov.pr.idr.application.livestock.breed.create.CreateBreedCommand;
import br.gov.pr.idr.application.livestock.breed.create.CreateBreedUseCase;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateBreedUseCase")
class CreateBreedUseCaseTest {

    @Mock BreedGateway gateway;
    @InjectMocks CreateBreedUseCase useCase;

    @Test
    void shouldCreateBreed() {
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(new CreateBreedCommand("Nelore"));
        assertEquals("Nelore", output.breedName());
        verify(gateway).save(any());
    }
}
