package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.producer.create.CreateProducerCommand;
import br.gov.pr.idr.application.property_management.producer.create.CreateProducerUseCase;
import br.gov.pr.idr.application.property_management.producer.retrieve.get.GetProducerByIdUseCase;
import br.gov.pr.idr.application.property_management.producer.retrieve.search.SearchProducerUseCase;
import br.gov.pr.idr.application.property_management.producer.update.UpdateProducerCommand;
import br.gov.pr.idr.application.property_management.producer.update.UpdateProducerUseCase;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.producer.models.create.CreateProducerRequest;
import br.gov.pr.idr.infra.property_management.producer.models.create.CreateProducerResponse;
import br.gov.pr.idr.infra.property_management.producer.models.get.GetProducerByIdResponse;
import br.gov.pr.idr.infra.property_management.producer.models.search.SearchProducerResponse;
import br.gov.pr.idr.infra.property_management.producer.models.update.UpdateProducerRequest;
import br.gov.pr.idr.infra.property_management.producer.models.update.UpdateProducerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/v1/producers")
@RequiredArgsConstructor
@RestController
public class ProducerController {

    private final CreateProducerUseCase createProducerUseCase;
    private final UpdateProducerUseCase updateProducerUseCase;
    private final GetProducerByIdUseCase getProducerByIdUseCase;
    private final SearchProducerUseCase searchProducerUseCase;

    @PostMapping
    public ResponseEntity<CreateProducerResponse> create(@RequestBody @Valid CreateProducerRequest request) {
        final var command = CreateProducerCommand.from(request.name(), request.cpf());
        return ResponseEntity.ok(CreateProducerResponse.from(createProducerUseCase.execute(command)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProducerResponse> update(@PathVariable UUID id,
                                                          @RequestBody @Valid UpdateProducerRequest request) {
        final var command = UpdateProducerCommand.from(id, request.name(), request.cpf());
        return ResponseEntity.ok(UpdateProducerResponse.from(updateProducerUseCase.execute(command)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProducerByIdResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(GetProducerByIdResponse.from(getProducerByIdUseCase.execute(id)));
    }

    @GetMapping("/search")
    public Pagination<SearchProducerResponse> search(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "name") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return searchProducerUseCase.execute(query).map(SearchProducerResponse::from);
    }
}
