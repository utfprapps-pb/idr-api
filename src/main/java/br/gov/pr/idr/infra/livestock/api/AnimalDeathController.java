package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.animal_death.delete.DeleteAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.delete.DeleteAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.get.GetAnimalDeathByIdCommand;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.get.GetAnimalDeathByIdOutput;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.get.GetAnimalDeathByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.list.ListAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.list.ListAnimalDeathOutput;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.list.ListAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_death.models.CreateAnimalDeathRequest;
import br.gov.pr.idr.infra.livestock.animal_death.models.CreateAnimalDeathResponse;
import br.gov.pr.idr.infra.livestock.animal_death.models.update.UpdateAnimalDeathRequest;
import br.gov.pr.idr.infra.livestock.animal_death.models.update.UpdateAnimalDeathResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import br.gov.pr.idr.infra.shared.support.AuthenticationSupport;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/deaths")
@RequiredArgsConstructor
@RestController
public class AnimalDeathController {

    private final CreateAnimalDeathUseCase createAnimalDeathUseCase;
    private final UpdateAnimalDeathUseCase updateAnimalDeathUseCase;
    private final DeleteAnimalDeathUseCase deleteAnimalDeathUseCase;
    private final GetAnimalDeathByIdUseCase getAnimalDeathByIdUseCase;
    private final ListAnimalDeathUseCase listAnimalDeathUseCase;

    @PostMapping
    public ResponseEntity<CreateAnimalDeathResponse> create(@PathVariable UUID propertyId,
                                                            @PathVariable UUID animalId,
                                                            final Authentication authentication,
                                                            @RequestBody @Valid
                                                            CreateAnimalDeathRequest request) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var command = new CreateAnimalDeathCommand(propertyId, animalId, scope,
                request.reason(), request.deathDate());
        final var output = createAnimalDeathUseCase.execute(command);
        return ResponseEntity.ok(CreateAnimalDeathResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateAnimalDeathResponse> update(@PathVariable UUID propertyId,
                                                            @PathVariable UUID animalId,
                                                            @PathVariable UUID id,
                                                            final Authentication authentication,
                                                            @RequestBody @Valid
                                                            UpdateAnimalDeathRequest request) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var command = new UpdateAnimalDeathCommand(propertyId, animalId, id, scope,
                request.reason(), request.deathDate());
        final var output = updateAnimalDeathUseCase.execute(command);
        return ResponseEntity.ok(UpdateAnimalDeathResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                       @PathVariable UUID animalId,
                                       @PathVariable UUID id,
                                       final Authentication authentication) {
        final var scope = AuthenticationSupport.scope(authentication);
        deleteAnimalDeathUseCase.execute(new DeleteAnimalDeathCommand(propertyId, animalId, id,
                scope));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAnimalDeathByIdOutput> getById(@PathVariable UUID propertyId,
                                                            @PathVariable UUID animalId,
                                                            @PathVariable UUID id,
                                                            final Authentication authentication) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var output = getAnimalDeathByIdUseCase.execute(
                new GetAnimalDeathByIdCommand(propertyId, animalId, id, scope));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListAnimalDeathOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "deathDate") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listAnimalDeathUseCase.execute(
                new ListAnimalDeathCommand(propertyId, animalId, scope, query));
        return ResponseEntity.ok(output);
    }
}
