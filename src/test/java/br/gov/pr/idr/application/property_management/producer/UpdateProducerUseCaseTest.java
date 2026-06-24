package br.gov.pr.idr.application.property_management.producer;

import br.gov.pr.idr.application.property_management.producer.update.UpdateProducerCommand;
import br.gov.pr.idr.application.property_management.producer.update.UpdateProducerUseCase;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.shared.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateProducerUseCase")
class UpdateProducerUseCaseTest {

    @Mock ProducerGateway producerGateway;
    @InjectMocks UpdateProducerUseCase useCase;

    private static final String CPF_A = "529.982.247-25";
    private static final String CPF_B = "111.444.777-35";

    @Test
    @DisplayName("deve atualizar produtor com sucesso")
    void shouldUpdateProducer() {
        final var producer = Producer.create("João", CPF.from(CPF_A));
        when(producerGateway.findById(any())).thenReturn(Optional.of(producer));
        when(producerGateway.update(any())).thenReturn(producer);

        final var output = useCase.execute(UpdateProducerCommand.from(UUID.randomUUID(), "João Atualizado", CPF_A));

        assertNotNull(output);
        verify(producerGateway).update(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando produtor não é encontrado")
    void shouldThrowWhenNotFound() {
        when(producerGateway.findById(any())).thenReturn(Optional.empty());
        var command = UpdateProducerCommand.from(UUID.randomUUID(), "João", CPF_A);
        assertThrows(NotFoundException.class,
                () -> useCase.execute(command));
    }

    @Test
    @DisplayName("deve lançar DomainException quando novo CPF já pertence a outro produtor")
    void shouldThrowWhenNewCpfAlreadyUsed() {
        final var producer = Producer.create("João", CPF.from(CPF_A));
        when(producerGateway.findById(any())).thenReturn(Optional.of(producer));
        when(producerGateway.existsByCpf(any())).thenReturn(true);
        var command = UpdateProducerCommand.from(UUID.randomUUID(), "João", CPF_B);
        assertThrows(DomainException.class,
                () -> useCase.execute(command));
    }
}
