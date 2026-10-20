package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal_purchases.create.CreateAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.create.CreateAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.delete.DeleteAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.delete.DeleteAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get.GetAnimalPurchasesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get.GetAnimalPurchasesByIdOutput;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get.GetAnimalPurchasesByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesOutput;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_purchases.models.CreateAnimalPurchasesRequest;
import br.gov.pr.idr.infra.livestock.animal_purchases.models.CreateAnimalPurchasesResponse;
import br.gov.pr.idr.infra.livestock.animal_purchases.models.update.UpdateAnimalPurchasesRequest;
import br.gov.pr.idr.infra.livestock.animal_purchases.models.update.UpdateAnimalPurchasesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import br.gov.pr.idr.infra.shared.support.AuthenticationSupport;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/purchases")
@RequiredArgsConstructor
@RestController
public class AnimalPurchasesController {

    private final CreateAnimalPurchasesUseCase createAnimalPurchasesUseCase;
    private final UpdateAnimalPurchasesUseCase updateAnimalPurchasesUseCase;
    private final DeleteAnimalPurchasesUseCase deleteAnimalPurchasesUseCase;
    private final GetAnimalPurchasesByIdUseCase getAnimalPurchasesByIdUseCase;
    private final ListAnimalPurchasesUseCase listAnimalPurchasesUseCase;

    @PostMapping
    public ResponseEntity<CreateAnimalPurchasesResponse> create(@PathVariable UUID propertyId,
                                                                  @PathVariable UUID animalId,
                                                                  final Authentication authentication,
                                                                  @RequestBody @Valid
                                                                  CreateAnimalPurchasesRequest request) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var command = new CreateAnimalPurchasesCommand(propertyId, animalId, scope,
                request.datePurchase(), request.birthDate(), request.value(), request.weight());
        final var output = createAnimalPurchasesUseCase.execute(command);
        return ResponseEntity.ok(CreateAnimalPurchasesResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateAnimalPurchasesResponse> update(@PathVariable UUID propertyId,
                                                                  @PathVariable UUID animalId,
                                                                  @PathVariable UUID id,
                                                                  final Authentication authentication,
                                                                  @RequestBody @Valid
                                                                  UpdateAnimalPurchasesRequest request) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var command = new UpdateAnimalPurchasesCommand(propertyId, animalId, id, scope,
                request.datePurchase(), request.birthDate(), request.value(), request.weight());
        final var output = updateAnimalPurchasesUseCase.execute(command);
        return ResponseEntity.ok(UpdateAnimalPurchasesResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID animalId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var scope = AuthenticationSupport.scope(authentication);
        deleteAnimalPurchasesUseCase.execute(new DeleteAnimalPurchasesCommand(propertyId, animalId, id,
                scope));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAnimalPurchasesByIdOutput> getById(@PathVariable UUID propertyId,
                                                                  @PathVariable UUID animalId,
                                                                  @PathVariable UUID id,
                                                                  final Authentication authentication) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var output = getAnimalPurchasesByIdUseCase.execute(
                new GetAnimalPurchasesByIdCommand(propertyId, animalId, id, scope));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListAnimalPurchasesOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "datePurchase") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var scope = AuthenticationSupport.scope(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listAnimalPurchasesUseCase.execute(
                new ListAnimalPurchasesCommand(propertyId, animalId, scope, query));
        return ResponseEntity.ok(output);
    }
}
