package br.gov.pr.idr.application.property_management.producer;

import br.gov.pr.idr.application.property_management.producer.retrieve.get.GetProducerByIdUseCase;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetProducerByIdUseCase")
class GetProducerByIdUseCaseTest {

    @Mock ProducerGateway producerGateway;
    @InjectMocks GetProducerByIdUseCase useCase;

    @Test
    @DisplayName("deve retornar output quando produtor é encontrado")
    void shouldReturnOutputWhenFound() {
        final var producer = Producer.create("Agricultor José", CPF.from("529.982.247-25"));
        when(producerGateway.findById(any())).thenReturn(Optional.of(producer));

        final var output = useCase.execute(UUID.randomUUID());

        assertNotNull(output);
        assertEquals("Agricultor José", output.name());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando produtor não é encontrado")
    void shouldThrowWhenNotFound() {
        when(producerGateway.findById(any())).thenReturn(Optional.empty());
        UUID uuid = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> useCase.execute(uuid));
    }
}
