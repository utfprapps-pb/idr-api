package br.gov.pr.idr.application.property_management.producer;

import br.gov.pr.idr.application.property_management.producer.create.CreateProducerCommand;
import br.gov.pr.idr.application.property_management.producer.create.CreateProducerUseCase;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProducerUseCase")
class CreateProducerUseCaseTest {

    @Mock ProducerGateway producerGateway;
    @InjectMocks CreateProducerUseCase useCase;

    private static final String VALID_CPF = "529.982.247-25";

    @Test
    @DisplayName("deve criar produtor com sucesso")
    void shouldCreateProducer() {
        final var producer = Producer.create("João da Silva", null);
        when(producerGateway.existsByCpf(any())).thenReturn(false);
        when(producerGateway.save(any())).thenReturn(producer);

        final var command = CreateProducerCommand.from("João da Silva", VALID_CPF);
        final var output = useCase.execute(command);

        assertNotNull(output);
        verify(producerGateway).save(any());
    }

    @Test
    @DisplayName("deve lançar DomainException quando CPF já existe")
    void shouldThrowWhenCpfAlreadyExists() {
        when(producerGateway.existsByCpf(any())).thenReturn(true);

        final var command = new CreateProducerCommand("João", VALID_CPF);
        assertThrows(DomainException.class, () -> useCase.execute(command));
        verify(producerGateway, never()).save(any());
    }
}
