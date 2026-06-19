package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.region.create.CreateRegionCommand;
import br.gov.pr.idr.application.property_management.region.create.CreateRegionUseCase;
import br.gov.pr.idr.application.property_management.region.delete.DeleteRegionUseCase;
import br.gov.pr.idr.application.property_management.region.update.UpdateRegionCommand;
import br.gov.pr.idr.application.property_management.region.update.UpdateRegionUseCase;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.region.models.create.CreateRegionRequest;
import br.gov.pr.idr.infra.property_management.region.models.create.CreateRegionResponse;
import br.gov.pr.idr.infra.property_management.region.models.list.ListRegionOutput;
import br.gov.pr.idr.infra.property_management.region.models.list.ListRegionUseCase;
import br.gov.pr.idr.infra.property_management.region.models.update.UpdateRegionRequest;
import br.gov.pr.idr.infra.property_management.region.models.update.UpdateRegionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/v1/regions")
@RequiredArgsConstructor
@RestController
public class RegionController {

    private final CreateRegionUseCase createRegionUseCase;
    private final UpdateRegionUseCase updateRegionUseCase;
    private final ListRegionUseCase listRegionUseCase;
    private final DeleteRegionUseCase deleteRegionUseCase;

    @PostMapping
    public ResponseEntity<CreateRegionResponse> create(@RequestBody @Valid CreateRegionRequest request) {
        final var command = new CreateRegionCommand(request.description());
        final var output = createRegionUseCase.execute(command);
        return ResponseEntity.ok(CreateRegionResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateRegionResponse> update(@PathVariable UUID id,
                                                       @RequestBody @Valid UpdateRegionRequest request) {
        final var command = new UpdateRegionCommand(id, request.description());
        final var output = updateRegionUseCase.execute(command);
        return ResponseEntity.ok(UpdateRegionResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteRegionUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListRegionOutput>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "") String terms,
            @RequestParam(defaultValue = "description") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return ResponseEntity.ok(listRegionUseCase.execute(query));
    }
}
