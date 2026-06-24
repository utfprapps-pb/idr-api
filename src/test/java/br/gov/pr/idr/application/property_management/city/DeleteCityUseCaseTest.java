package br.gov.pr.idr.application.property_management.city;

import br.gov.pr.idr.application.property_management.city.delete.DeleteCityUseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;
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
@DisplayName("DeleteCityUseCase")
class DeleteCityUseCaseTest {

    @Mock CityGateway cityGateway;
    @InjectMocks DeleteCityUseCase useCase;

    @Test
    @DisplayName("deve deletar cidade existente")
    void shouldDeleteExistingCity() {
        when(cityGateway.existsById(any())).thenReturn(true);

        useCase.execute(UUID.randomUUID());

        verify(cityGateway).deleteById(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando cidade não existe")
    void shouldThrowWhenCityNotFound() {
        when(cityGateway.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
        verify(cityGateway, never()).deleteById(any());
    }
}
