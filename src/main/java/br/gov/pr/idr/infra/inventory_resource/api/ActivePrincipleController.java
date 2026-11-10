package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.active_principle.create.CreateActivePrincipleCommand;
import br.gov.pr.idr.application.inventory_resource.active_principle.create.CreateActivePrincipleUseCase;
import br.gov.pr.idr.application.inventory_resource.active_principle.delete.DeleteActivePrincipleUseCase;
import br.gov.pr.idr.application.inventory_resource.active_principle.retrieve.list.ListActivePrincipleOutput;
import br.gov.pr.idr.application.inventory_resource.active_principle.retrieve.list.ListActivePrincipleUseCase;
import br.gov.pr.idr.application.inventory_resource.active_principle.update.UpdateActivePrincipleCommand;
import br.gov.pr.idr.application.inventory_resource.active_principle.update.UpdateActivePrincipleUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.active_principle.models.CreateActivePrincipleRequest;
import br.gov.pr.idr.infra.inventory_resource.active_principle.models.CreateActivePrincipleResponse;
import br.gov.pr.idr.infra.inventory_resource.active_principle.models.update.UpdateActivePrincipleRequest;
import br.gov.pr.idr.infra.inventory_resource.active_principle.models.update.UpdateActivePrincipleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("activePrinciples")
@RequiredArgsConstructor
@RestController
public class ActivePrincipleController {

    private final CreateActivePrincipleUseCase createActivePrincipleUseCase;
    private final UpdateActivePrincipleUseCase updateActivePrincipleUseCase;
    private final ListActivePrincipleUseCase listActivePrincipleUseCase;
    private final DeleteActivePrincipleUseCase deleteActivePrincipleUseCase;

    @PostMapping
    public ResponseEntity<CreateActivePrincipleResponse> create(@RequestBody @Valid CreateActivePrincipleRequest request) {
        final var command = new CreateActivePrincipleCommand(request.name());
        final var output = createActivePrincipleUseCase.execute(command);
        return ResponseEntity.ok(CreateActivePrincipleResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateActivePrincipleResponse> update(@PathVariable UUID id,
                                                                  @RequestBody @Valid UpdateActivePrincipleRequest request) {
        final var command = new UpdateActivePrincipleCommand(id, request.name());
        final var output = updateActivePrincipleUseCase.execute(command);
        return ResponseEntity.ok(UpdateActivePrincipleResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteActivePrincipleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListActivePrincipleOutput>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "") String terms,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return ResponseEntity.ok(listActivePrincipleUseCase.execute(query));
    }
}
