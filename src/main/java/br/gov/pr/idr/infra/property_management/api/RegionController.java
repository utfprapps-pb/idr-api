package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.region.create.CreateRegionCommand;
import br.gov.pr.idr.application.property_management.region.create.CreateRegionUseCase;
import br.gov.pr.idr.infra.property_management.region.models.CreateRegionRequest;
import br.gov.pr.idr.infra.property_management.region.models.CreateRegionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/regions")
@RequiredArgsConstructor
@RestController
public class RegionController {

    private final CreateRegionUseCase createRegionUseCase;

    @PostMapping
    public ResponseEntity<CreateRegionResponse> create(@RequestBody @Valid CreateRegionRequest request) {
        final var command = new CreateRegionCommand(request.description());
        final var output = createRegionUseCase.execute(command);
        return ResponseEntity.ok(CreateRegionResponse.from(output));
    }
}
