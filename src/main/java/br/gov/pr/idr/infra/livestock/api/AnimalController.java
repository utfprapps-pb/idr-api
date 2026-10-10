package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalUseCase;
import br.gov.pr.idr.application.livestock.animal.delete.DeleteAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.delete.DeleteAnimalUseCase;
import br.gov.pr.idr.application.livestock.animal.retrieve.find.FindAnimalByIdentifierCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.find.FindAnimalByIdentifierUseCase;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdOutput;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdUseCase;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalOutput;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalUseCase;
import br.gov.pr.idr.application.livestock.animal.update.UpdateAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.update.UpdateAnimalUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal.models.CreateAnimalRequest;
import br.gov.pr.idr.infra.livestock.animal.models.CreateAnimalResponse;
import br.gov.pr.idr.infra.livestock.animal.models.update.UpdateAnimalRequest;
import br.gov.pr.idr.infra.livestock.animal.models.update.UpdateAnimalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import br.gov.pr.idr.infra.shared.support.AuthenticationSupport;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals")
@RequiredArgsConstructor
@RestController
public class AnimalController {

    private final CreateAnimalUseCase createAnimalUseCase;
    private final UpdateAnimalUseCase updateAnimalUseCase;
    private final DeleteAnimalUseCase deleteAnimalUseCase;
    private final GetAnimalByIdUseCase getAnimalByIdUseCase;
    private final ListAnimalUseCase listAnimalUseCase;
    private final FindAnimalByIdentifierUseCase findAnimalByIdentifierUseCase;

    @PostMapping
    public ResponseEntity<CreateAnimalResponse> create(@PathVariable UUID propertyId,
                                                         final Authentication authentication,
                                                         @RequestBody @Valid CreateAnimalRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new CreateAnimalCommand(propertyId, username, request.animalMotherId(),
                request.breedId(), request.type(), request.identifier(), request.gender(),
                request.bornCondition(), request.bornDate(), request.bornWeight(), request.previousWeight(),
                request.currentWeight(), request.ecc());
        final var output = createAnimalUseCase.execute(command);
        return ResponseEntity.ok(CreateAnimalResponse.from(output));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateAnimalResponse> update(@PathVariable UUID propertyId,
                                                        @PathVariable UUID id,
                                                        final Authentication authentication,
                                                        @RequestBody @Valid UpdateAnimalRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new UpdateAnimalCommand(propertyId, id, username, request.animalMotherId(),
                request.breedId(), request.type(), request.identifier(), request.gender(),
                request.bornCondition(), request.bornDate(), request.bornWeight(), request.previousWeight(),
                request.currentWeight(), request.ecc());
        final var output = updateAnimalUseCase.execute(command);
        return ResponseEntity.ok(UpdateAnimalResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        deleteAnimalUseCase.execute(new DeleteAnimalCommand(propertyId, id, username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAnimalByIdOutput> getById(@PathVariable UUID propertyId,
                                                        @PathVariable UUID id,
                                                        final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        final var output = getAnimalByIdUseCase.execute(new GetAnimalByIdCommand(propertyId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListAnimalOutput>> search(
            @PathVariable UUID propertyId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "identifier") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = AuthenticationSupport.username(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listAnimalUseCase.execute(new ListAnimalCommand(propertyId, username, query));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/findAnimal/{identifier}")
    public ResponseEntity<GetAnimalByIdOutput> findByIdentifier(@PathVariable UUID propertyId,
                                                                 @PathVariable String identifier,
                                                                 final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new FindAnimalByIdentifierCommand(propertyId, identifier, username);
        return findAnimalByIdentifierUseCase.execute(command)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
