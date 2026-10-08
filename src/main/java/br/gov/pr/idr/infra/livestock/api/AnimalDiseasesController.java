package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal_diseases.create.CreateAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.create.CreateAnimalDiseasesUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.delete.DeleteAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.delete.DeleteAnimalDiseasesUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdOutput;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list.ListAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list.ListAnimalDiseasesOutput;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list.ListAnimalDiseasesUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_diseases.models.CreateAnimalDiseasesRequest;
import br.gov.pr.idr.infra.livestock.animal_diseases.models.CreateAnimalDiseasesResponse;
import br.gov.pr.idr.infra.livestock.animal_diseases.models.update.UpdateAnimalDiseasesRequest;
import br.gov.pr.idr.infra.livestock.animal_diseases.models.update.UpdateAnimalDiseasesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/animalDiseases")
@RequiredArgsConstructor
@RestController
public class AnimalDiseasesController {

    private final CreateAnimalDiseasesUseCase createAnimalDiseasesUseCase;
    private final UpdateAnimalDiseasesUseCase updateAnimalDiseasesUseCase;
    private final DeleteAnimalDiseasesUseCase deleteAnimalDiseasesUseCase;
    private final GetAnimalDiseasesByIdUseCase getAnimalDiseasesByIdUseCase;
    private final ListAnimalDiseasesUseCase listAnimalDiseasesUseCase;

    @PostMapping
    public ResponseEntity<CreateAnimalDiseasesResponse> create(@PathVariable UUID propertyId,
                                                                  @PathVariable UUID animalId,
                                                                  final Authentication authentication,
                                                                  @RequestBody @Valid
                                                                  CreateAnimalDiseasesRequest request) {
        final var username = this.getUsername(authentication);
        final var command = new CreateAnimalDiseasesCommand(propertyId, animalId, username,
                request.diagnosis(), request.diagnosisDate());
        final var output = createAnimalDiseasesUseCase.execute(command);
        return ResponseEntity.ok(CreateAnimalDiseasesResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateAnimalDiseasesResponse> update(@PathVariable UUID propertyId,
                                                                  @PathVariable UUID animalId,
                                                                  @PathVariable UUID id,
                                                                  final Authentication authentication,
                                                                  @RequestBody @Valid
                                                                  UpdateAnimalDiseasesRequest request) {
        final var username = this.getUsername(authentication);
        final var command = new UpdateAnimalDiseasesCommand(propertyId, animalId, id, username,
                request.diagnosis(), request.diagnosisDate());
        final var output = updateAnimalDiseasesUseCase.execute(command);
        return ResponseEntity.ok(UpdateAnimalDiseasesResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID animalId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var username = this.getUsername(authentication);
        deleteAnimalDiseasesUseCase.execute(new DeleteAnimalDiseasesCommand(propertyId, animalId, id,
                username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAnimalDiseasesByIdOutput> getById(@PathVariable UUID propertyId,
                                                                  @PathVariable UUID animalId,
                                                                  @PathVariable UUID id,
                                                                  final Authentication authentication) {
        final var username = this.getUsername(authentication);
        final var output = getAnimalDiseasesByIdUseCase.execute(
                new GetAnimalDiseasesByIdCommand(propertyId, animalId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListAnimalDiseasesOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "diagnosisDate") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = this.getUsername(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listAnimalDiseasesUseCase.execute(
                new ListAnimalDiseasesCommand(propertyId, animalId, username, query));
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
