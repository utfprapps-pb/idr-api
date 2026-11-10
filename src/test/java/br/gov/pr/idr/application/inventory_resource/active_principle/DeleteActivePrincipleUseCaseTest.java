package br.gov.pr.idr.application.inventory_resource.active_principle;

import br.gov.pr.idr.application.inventory_resource.active_principle.delete.DeleteActivePrincipleUseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteActivePrincipleUseCase")
class DeleteActivePrincipleUseCaseTest {

    @Mock ActivePrincipleGateway gateway;
    @InjectMocks DeleteActivePrincipleUseCase useCase;

    @Test
    @DisplayName("deve deletar princípio ativo existente")
    void shouldDeleteExistingPrinciple() {
        when(gateway.existsById(any())).thenReturn(true);

        useCase.execute(UUID.randomUUID());

        verify(gateway).deleteById(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistentPrinciple() {
        when(gateway.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
    }
}
