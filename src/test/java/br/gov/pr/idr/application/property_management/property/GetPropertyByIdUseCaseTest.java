package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.retrieve.get.GetPropertyByIdUseCase;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetPropertyByIdUseCase")
class GetPropertyByIdUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @InjectMocks GetPropertyByIdUseCase useCase;

    @Test
    @DisplayName("deve retornar resultado quando propriedade é encontrada")
    void shouldReturnResultWhenFound() {
        final var result = new GetPropertyQueryResult(
                UUID.randomUUID(), "Fazenda",
                null, null,
                0.0, 0.0, 0.0, 0.0,
                null, null,
                UUID.randomUUID(), "Produtor",
                UUID.randomUUID(), "Cidade",
                null, null
        );
        when(propertyGateway.findByIdWithDetails(any())).thenReturn(Optional.of(result));

        final var output = useCase.execute(UUID.randomUUID());

        assertNotNull(output);
        assertEquals("Fazenda", output.name());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando propriedade não é encontrada")
    void shouldThrowWhenNotFound() {
        when(propertyGateway.findByIdWithDetails(any())).thenReturn(Optional.empty());
        var id = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> useCase.execute(id));
    }
}
