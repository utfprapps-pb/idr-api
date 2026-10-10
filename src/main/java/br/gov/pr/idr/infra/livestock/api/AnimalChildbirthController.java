package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal_childbirth.create.CreateAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.create.CreateAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.delete.DeleteAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.delete.DeleteAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdOutput;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthOutput;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_childbirth.models.CreateAnimalChildbirthRequest;
import br.gov.pr.idr.infra.livestock.animal_childbirth.models.CreateAnimalChildbirthResponse;
import br.gov.pr.idr.infra.livestock.animal_childbirth.models.update.UpdateAnimalChildbirthRequest;
import br.gov.pr.idr.infra.livestock.animal_childbirth.models.update.UpdateAnimalChildbirthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import br.gov.pr.idr.infra.shared.support.AuthenticationSupport;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/childbirths")
@RequiredArgsConstructor
@RestController
public class AnimalChildbirthController {

    private final CreateAnimalChildbirthUseCase createAnimalChildbirthUseCase;
    private final UpdateAnimalChildbirthUseCase updateAnimalChildbirthUseCase;
    private final DeleteAnimalChildbirthUseCase deleteAnimalChildbirthUseCase;
    private final GetAnimalChildbirthByIdUseCase getAnimalChildbirthByIdUseCase;
    private final ListAnimalChildbirthUseCase listAnimalChildbirthUseCase;

    @PostMapping
    public ResponseEntity<CreateAnimalChildbirthResponse> create(@PathVariable UUID propertyId,
                                                                 @PathVariable UUID animalId,
                                                                 final Authentication authentication,
                                                                 @RequestBody @Valid
                                                                 CreateAnimalChildbirthRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new CreateAnimalChildbirthCommand(propertyId, animalId, username,
                request.childbirthDate(), request.gender(), request.weight(), request.condition(),
                request.breedId(), request.calfId());
        final var output = createAnimalChildbirthUseCase.execute(command);
        return ResponseEntity.ok(CreateAnimalChildbirthResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateAnimalChildbirthResponse> update(@PathVariable UUID propertyId,
                                                                 @PathVariable UUID animalId,
                                                                 @PathVariable UUID id,
                                                                 final Authentication authentication,
                                                                 @RequestBody @Valid
                                                                 UpdateAnimalChildbirthRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new UpdateAnimalChildbirthCommand(propertyId, animalId, id, username,
                request.childbirthDate(), request.gender(), request.weight(), request.condition(),
                request.breedId(), request.calfId());
        final var output = updateAnimalChildbirthUseCase.execute(command);
        return ResponseEntity.ok(UpdateAnimalChildbirthResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                       @PathVariable UUID animalId,
                                       @PathVariable UUID id,
                                       final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        deleteAnimalChildbirthUseCase.execute(new DeleteAnimalChildbirthCommand(propertyId, animalId, id,
                username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAnimalChildbirthByIdOutput> getById(@PathVariable UUID propertyId,
                                                                 @PathVariable UUID animalId,
                                                                 @PathVariable UUID id,
                                                                 final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        final var output = getAnimalChildbirthByIdUseCase.execute(
                new GetAnimalChildbirthByIdCommand(propertyId, animalId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListAnimalChildbirthOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "childbirthDate") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = AuthenticationSupport.username(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listAnimalChildbirthUseCase.execute(
                new ListAnimalChildbirthCommand(propertyId, animalId, username, query));
        return ResponseEntity.ok(output);
    }
}
