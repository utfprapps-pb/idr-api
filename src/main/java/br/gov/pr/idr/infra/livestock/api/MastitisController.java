package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisUseCase;
import br.gov.pr.idr.application.livestock.mastitis.delete.DeleteMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.delete.DeleteMastitisUseCase;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdCommand;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdOutput;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdUseCase;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.list.ListMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.list.ListMastitisOutput;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.list.ListMastitisUseCase;
import br.gov.pr.idr.application.livestock.mastitis.update.UpdateMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.update.UpdateMastitisUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.mastitis.models.CreateMastitisRequest;
import br.gov.pr.idr.infra.livestock.mastitis.models.CreateMastitisResponse;
import br.gov.pr.idr.infra.livestock.mastitis.models.update.UpdateMastitisRequest;
import br.gov.pr.idr.infra.livestock.mastitis.models.update.UpdateMastitisResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/mastitis")
@RequiredArgsConstructor
@RestController
public class MastitisController {

    private final CreateMastitisUseCase createMastitisUseCase;
    private final UpdateMastitisUseCase updateMastitisUseCase;
    private final DeleteMastitisUseCase deleteMastitisUseCase;
    private final GetMastitisByIdUseCase getMastitisByIdUseCase;
    private final ListMastitisUseCase listMastitisUseCase;

    @PostMapping
    public ResponseEntity<CreateMastitisResponse> create(@PathVariable UUID propertyId,
                                                           @PathVariable UUID animalId,
                                                           final Authentication authentication,
                                                           @RequestBody @Valid CreateMastitisRequest request) {
        final var username = this.getUsername(authentication);
        final var command = new CreateMastitisCommand(propertyId, animalId, username, request.diagnoseDate(),
                request.mastitisType(), request.cmtResult(), request.ad(), request.ae(), request.pd(),
                request.pe());
        final var output = createMastitisUseCase.execute(command);
        return ResponseEntity.ok(CreateMastitisResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateMastitisResponse> update(@PathVariable UUID propertyId,
                                                           @PathVariable UUID animalId,
                                                           @PathVariable UUID id,
                                                           final Authentication authentication,
                                                           @RequestBody @Valid UpdateMastitisRequest request) {
        final var username = this.getUsername(authentication);
        final var command = new UpdateMastitisCommand(propertyId, animalId, id, username, request.diagnoseDate(),
                request.mastitisType(), request.cmtResult(), request.ad(), request.ae(), request.pd(),
                request.pe());
        final var output = updateMastitisUseCase.execute(command);
        return ResponseEntity.ok(UpdateMastitisResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID animalId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var username = this.getUsername(authentication);
        deleteMastitisUseCase.execute(new DeleteMastitisCommand(propertyId, animalId, id, username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetMastitisByIdOutput> getById(@PathVariable UUID propertyId,
                                                           @PathVariable UUID animalId,
                                                           @PathVariable UUID id,
                                                           final Authentication authentication) {
        final var username = this.getUsername(authentication);
        final var output = getMastitisByIdUseCase.execute(
                new GetMastitisByIdCommand(propertyId, animalId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListMastitisOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "diagnoseDate") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = this.getUsername(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listMastitisUseCase.execute(
                new ListMastitisCommand(propertyId, animalId, username, query));
        return ResponseEntity.ok(output);
    }

    private String getUsername(final Authentication authentication) {
        final var principal = authentication == null ? null : authentication.getPrincipal();
        if (principal == null) {
            throw new IllegalStateException("Authentication principal is null");
        }
        return principal.toString();
    }
}
