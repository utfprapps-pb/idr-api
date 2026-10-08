package br.gov.pr.idr.application.inventory_resource.active_principle;

import br.gov.pr.idr.application.inventory_resource.active_principle.create.CreateActivePrincipleCommand;
import br.gov.pr.idr.application.inventory_resource.active_principle.create.CreateActivePrincipleUseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
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
@DisplayName("CreateActivePrincipleUseCase")
class CreateActivePrincipleUseCaseTest {

    @Mock ActivePrincipleGateway gateway;
    @InjectMocks CreateActivePrincipleUseCase useCase;

    @Test
    void shouldCreateActivePrinciple() {
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(new CreateActivePrincipleCommand("Amoxicilina"));
        assertEquals("Amoxicilina", output.name());
        verify(gateway).save(any());
    }
}
