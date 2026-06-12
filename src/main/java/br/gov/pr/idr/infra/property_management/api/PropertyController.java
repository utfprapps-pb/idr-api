package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyCommand;
import br.gov.pr.idr.application.property_management.property.create.CreatePropertyUseCase;
import br.gov.pr.idr.infra.property_management.property.models.CreatePropertyRequest;
import br.gov.pr.idr.infra.property_management.property.models.CreatePropertyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/properties")
@RequiredArgsConstructor
@RestController
public class PropertyController {

    private final CreatePropertyUseCase createPropertyUseCase;

    @PostMapping
    public ResponseEntity<CreatePropertyResponse> create(@RequestBody @Valid CreatePropertyRequest request) {
        final var command = CreatePropertyCommand.from(
                request.name(),
                request.latitude(),
                request.longitude(),
                request.totalArea(),
                request.leased(),
                request.nakedAveragePrice(),
                request.leaseAveragePrice(),
                request.dairyCattleFarming(),
                request.perennialPasture(),
                request.summerPlowing(),
                request.winterPlowing(),
                request.producerId(),
                request.cityId()
        );
        final var output = createPropertyUseCase.execute(command);
        return ResponseEntity.ok(CreatePropertyResponse.from(output));
    }
}
