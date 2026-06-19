package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyCommand;
import br.gov.pr.idr.application.property_management.property.create.CreatePropertyUseCase;
import br.gov.pr.idr.application.property_management.property.delete.DeletePropertyUseCase;
import br.gov.pr.idr.application.property_management.property.retrieve.get.GetPropertyByIdUseCase;
import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyUseCase;
import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyCommand;
import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyUseCase;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.property.models.create.CreatePropertyRequest;
import br.gov.pr.idr.infra.property_management.property.models.create.CreatePropertyResponse;
import br.gov.pr.idr.infra.property_management.property.models.search.SearchPropertyResponse;
import br.gov.pr.idr.infra.property_management.property.models.update.UpdatePropertyRequest;
import br.gov.pr.idr.infra.property_management.property.models.update.UpdatePropertyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import java.util.List;

@RequestMapping("/v1/properties")
@RequiredArgsConstructor
@RestController
public class PropertyController {

    private final CreatePropertyUseCase createPropertyUseCase;
    private final UpdatePropertyUseCase updatePropertyUseCase;
    private final GetPropertyByIdUseCase getPropertyByIdUseCase;
    private final SearchPropertyUseCase searchPropertyUseCase;
    private final DeletePropertyUseCase deletePropertyUseCase;

    @PostMapping
    public ResponseEntity<CreatePropertyResponse> create(@RequestBody @Valid CreatePropertyRequest request) {
        final var command = CreatePropertyCommand.from(
                request.name(),
                request.latitude(),
                request.longitude(),
                request.nakedAveragePrice(),
                request.leaseAveragePrice(),
                request.dairyCattleFarming(),
                request.perennialPasture(),
                request.summerPlowing(),
                request.winterPlowing(),
                request.producerId(),
                request.cityId(),
                request.technicianIds(),
                this.getCollaboratorData(request)
        );
        final var output = createPropertyUseCase.execute(command);
        return ResponseEntity.ok(CreatePropertyResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePropertyResponse> update(@PathVariable UUID id,
                                                         @RequestBody @Valid UpdatePropertyRequest request) {
        final var command = UpdatePropertyCommand.from(
                id,
                request.name(),
                request.latitude(),
                request.longitude(),
                request.nakedAveragePrice(),
                request.leaseAveragePrice(),
                request.dairyCattleFarming(),
                request.perennialPasture(),
                request.summerPlowing(),
                request.winterPlowing(),
                request.producerId(),
                request.cityId(),
                request.technicianIds(),
                request.collaborators() == null ? null : request.collaborators().stream()
                        .map(c -> new UpdatePropertyCommand.CollaboratorData(c.name(), c.hoursPerDay()))
                        .toList()
        );
        return ResponseEntity.ok(UpdatePropertyResponse.from(updatePropertyUseCase.execute(command)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPropertyQueryResult> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(getPropertyByIdUseCase.execute(id));
    }

    @GetMapping("/search")
    public Pagination<SearchPropertyResponse> search(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "name") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return searchPropertyUseCase.execute(query).map(SearchPropertyResponse::from);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deletePropertyUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private List<CreatePropertyCommand.CollaboratorData> getCollaboratorData(CreatePropertyRequest request) {
        return request.collaborators() == null ? null : request.collaborators().stream()
                   .map(c -> new CreatePropertyCommand.CollaboratorData(c.name(),
                                                                c.hoursPerDay()))
                                                               .toList();
    }
}
