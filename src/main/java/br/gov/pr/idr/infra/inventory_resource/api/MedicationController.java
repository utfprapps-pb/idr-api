package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.delete.DeleteMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.delete.DeleteMedicationUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdCommand;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdOutput;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.list.ListMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.list.ListMedicationOutput;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.list.ListMedicationUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.medication.models.CreateMedicationRequest;
import br.gov.pr.idr.infra.inventory_resource.medication.models.CreateMedicationResponse;
import br.gov.pr.idr.infra.inventory_resource.medication.models.update.UpdateMedicationRequest;
import br.gov.pr.idr.infra.inventory_resource.medication.models.update.UpdateMedicationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/medications")
@RequiredArgsConstructor
@RestController
public class MedicationController {

    private final CreateMedicationUseCase createMedicationUseCase;
    private final UpdateMedicationUseCase updateMedicationUseCase;
    private final DeleteMedicationUseCase deleteMedicationUseCase;
    private final GetMedicationByIdUseCase getMedicationByIdUseCase;
    private final ListMedicationUseCase listMedicationUseCase;

    @PostMapping
    public ResponseEntity<CreateMedicationResponse> create(@PathVariable UUID propertyId,
                                                             @PathVariable UUID animalId,
                                                             final Authentication authentication,
                                                             @RequestBody @Valid CreateMedicationRequest request) {
        final var username = this.getUsername(authentication);
        final var command = new CreateMedicationCommand(propertyId, animalId, username, request.productId(),
                request.applicationDate(), request.appliedDose(), request.applicationWay());
        final var output = createMedicationUseCase.execute(command);
        return ResponseEntity.ok(CreateMedicationResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateMedicationResponse> update(@PathVariable UUID propertyId,
                                                             @PathVariable UUID animalId,
                                                             @PathVariable UUID id,
                                                             final Authentication authentication,
                                                             @RequestBody @Valid UpdateMedicationRequest request) {
        final var username = this.getUsername(authentication);
        final var command = new UpdateMedicationCommand(propertyId, animalId, id, username, request.productId(),
                request.applicationDate(), request.appliedDose(), request.applicationWay());
        final var output = updateMedicationUseCase.execute(command);
        return ResponseEntity.ok(UpdateMedicationResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID animalId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var username = this.getUsername(authentication);
        deleteMedicationUseCase.execute(new DeleteMedicationCommand(propertyId, animalId, id, username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetMedicationByIdOutput> getById(@PathVariable UUID propertyId,
                                                             @PathVariable UUID animalId,
                                                             @PathVariable UUID id,
                                                             final Authentication authentication) {
        final var username = this.getUsername(authentication);
        final var output = getMedicationByIdUseCase.execute(
                new GetMedicationByIdCommand(propertyId, animalId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListMedicationOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "applicationDate") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = this.getUsername(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listMedicationUseCase.execute(
                new ListMedicationCommand(propertyId, animalId, username, query));
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
