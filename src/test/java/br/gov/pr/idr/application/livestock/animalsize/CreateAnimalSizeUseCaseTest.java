package br.gov.pr.idr.application.livestock.animalsize;

import br.gov.pr.idr.application.livestock.animalsize.create.CreateAnimalSizeCommand;
import br.gov.pr.idr.application.livestock.animalsize.create.CreateAnimalSizeUseCase;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
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
@DisplayName("CreateAnimalSizeUseCase")
class CreateAnimalSizeUseCaseTest {

    @Mock AnimalSizeGateway gateway;
    @InjectMocks CreateAnimalSizeUseCase useCase;

    @Test
    void shouldCreateAnimalSize() {
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(new CreateAnimalSizeCommand("Grande"));
        assertEquals("Grande", output.sizeName());
        verify(gateway).save(any());
    }
}
