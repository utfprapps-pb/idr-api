package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.delete.DeletePropertyUseCase;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeletePropertyUseCase")
class DeletePropertyUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @InjectMocks DeletePropertyUseCase useCase;

    @Test
    @DisplayName("deve deletar propriedade existente")
    void shouldDeleteExistingProperty() {
        when(propertyGateway.existsById(any())).thenReturn(true);

        useCase.execute(UUID.randomUUID());

        verify(propertyGateway).deleteById(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando propriedade não existe")
    void shouldThrowWhenPropertyNotFound() {
        when(propertyGateway.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
        verify(propertyGateway, never()).deleteById(any());
    }
}
