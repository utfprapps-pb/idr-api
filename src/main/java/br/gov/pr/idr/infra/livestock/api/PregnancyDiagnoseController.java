package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.delete.DeletePregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.delete.DeletePregnancyDiagnoseUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdOutput;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list.ListPregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list.ListPregnancyDiagnoseOutput;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list.ListPregnancyDiagnoseUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.update.UpdatePregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.update.UpdatePregnancyDiagnoseUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models.CreatePregnancyDiagnoseRequest;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models.CreatePregnancyDiagnoseResponse;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models.update.UpdatePregnancyDiagnoseRequest;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models.update.UpdatePregnancyDiagnoseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import br.gov.pr.idr.infra.shared.support.AuthenticationSupport;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/pregnancy-diagnoses")
@RequiredArgsConstructor
@RestController
public class PregnancyDiagnoseController {

    private final CreatePregnancyDiagnoseUseCase createPregnancyDiagnoseUseCase;
    private final UpdatePregnancyDiagnoseUseCase updatePregnancyDiagnoseUseCase;
    private final DeletePregnancyDiagnoseUseCase deletePregnancyDiagnoseUseCase;
    private final GetPregnancyDiagnoseByIdUseCase getPregnancyDiagnoseByIdUseCase;
    private final ListPregnancyDiagnoseUseCase listPregnancyDiagnoseUseCase;

    @PostMapping
    public ResponseEntity<CreatePregnancyDiagnoseResponse> create(@PathVariable UUID propertyId,
                                                                    @PathVariable UUID animalId,
                                                                    final Authentication authentication,
                                                                    @RequestBody @Valid
                                                                    CreatePregnancyDiagnoseRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new CreatePregnancyDiagnoseCommand(propertyId, animalId, username,
                request.diagnoseDate(), request.lastIa());
        final var output = createPregnancyDiagnoseUseCase.execute(command);
        return ResponseEntity.ok(CreatePregnancyDiagnoseResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePregnancyDiagnoseResponse> update(@PathVariable UUID propertyId,
                                                                    @PathVariable UUID animalId,
                                                                    @PathVariable UUID id,
                                                                    final Authentication authentication,
                                                                    @RequestBody @Valid
                                                                    UpdatePregnancyDiagnoseRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new UpdatePregnancyDiagnoseCommand(propertyId, animalId, id, username,
                request.diagnoseDate(), request.lastIa());
        final var output = updatePregnancyDiagnoseUseCase.execute(command);
        return ResponseEntity.ok(UpdatePregnancyDiagnoseResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID animalId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        deletePregnancyDiagnoseUseCase.execute(new DeletePregnancyDiagnoseCommand(propertyId, animalId, id,
                username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPregnancyDiagnoseByIdOutput> getById(@PathVariable UUID propertyId,
                                                                    @PathVariable UUID animalId,
                                                                    @PathVariable UUID id,
                                                                    final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        final var output = getPregnancyDiagnoseByIdUseCase.execute(
                new GetPregnancyDiagnoseByIdCommand(propertyId, animalId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListPregnancyDiagnoseOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "diagnoseDate") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = AuthenticationSupport.username(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listPregnancyDiagnoseUseCase.execute(
                new ListPregnancyDiagnoseCommand(propertyId, animalId, username, query));
        return ResponseEntity.ok(output);
    }
}
