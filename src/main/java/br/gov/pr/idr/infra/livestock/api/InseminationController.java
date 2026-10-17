package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.insemination.create.CreateInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.create.CreateInseminationUseCase;
import br.gov.pr.idr.application.livestock.insemination.delete.DeleteInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.delete.DeleteInseminationUseCase;
import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdCommand;
import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdOutput;
import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdUseCase;
import br.gov.pr.idr.application.livestock.insemination.retrieve.list.ListInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.retrieve.list.ListInseminationOutput;
import br.gov.pr.idr.application.livestock.insemination.retrieve.list.ListInseminationUseCase;
import br.gov.pr.idr.application.livestock.insemination.update.UpdateInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.update.UpdateInseminationUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.insemination.models.CreateInseminationRequest;
import br.gov.pr.idr.infra.livestock.insemination.models.CreateInseminationResponse;
import br.gov.pr.idr.infra.livestock.insemination.models.update.UpdateInseminationRequest;
import br.gov.pr.idr.infra.livestock.insemination.models.update.UpdateInseminationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import br.gov.pr.idr.infra.shared.support.AuthenticationSupport;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/inseminations")
@RequiredArgsConstructor
@RestController
public class InseminationController {

    private final CreateInseminationUseCase createInseminationUseCase;
    private final UpdateInseminationUseCase updateInseminationUseCase;
    private final DeleteInseminationUseCase deleteInseminationUseCase;
    private final GetInseminationByIdUseCase getInseminationByIdUseCase;
    private final ListInseminationUseCase listInseminationUseCase;

    @PostMapping
    public ResponseEntity<CreateInseminationResponse> create(@PathVariable UUID propertyId,
                                                               @PathVariable UUID animalId,
                                                               final Authentication authentication,
                                                               @RequestBody @Valid CreateInseminationRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new CreateInseminationCommand(propertyId, animalId, username, request.iaDate(),
                request.bull());
        final var output = createInseminationUseCase.execute(command);
        return ResponseEntity.ok(CreateInseminationResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateInseminationResponse> update(@PathVariable UUID propertyId,
                                                               @PathVariable UUID animalId,
                                                               @PathVariable UUID id,
                                                               final Authentication authentication,
                                                               @RequestBody @Valid UpdateInseminationRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new UpdateInseminationCommand(propertyId, animalId, id, username, request.iaDate(),
                request.bull());
        final var output = updateInseminationUseCase.execute(command);
        return ResponseEntity.ok(UpdateInseminationResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID animalId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        deleteInseminationUseCase.execute(new DeleteInseminationCommand(propertyId, animalId, id, username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetInseminationByIdOutput> getById(@PathVariable UUID propertyId,
                                                               @PathVariable UUID animalId,
                                                               @PathVariable UUID id,
                                                               final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        final var output = getInseminationByIdUseCase.execute(
                new GetInseminationByIdCommand(propertyId, animalId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListInseminationOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "iaDate") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = AuthenticationSupport.username(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listInseminationUseCase.execute(
                new ListInseminationCommand(propertyId, animalId, username, query));
        return ResponseEntity.ok(output);
    }
}
