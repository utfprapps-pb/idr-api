package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.delete.DeleteAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.delete.DeleteAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdOutput;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.list.ListAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.list.ListAnimalSalesOutput;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.list.ListAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_sales.models.CreateAnimalSalesRequest;
import br.gov.pr.idr.infra.livestock.animal_sales.models.CreateAnimalSalesResponse;
import br.gov.pr.idr.infra.livestock.animal_sales.models.update.UpdateAnimalSalesRequest;
import br.gov.pr.idr.infra.livestock.animal_sales.models.update.UpdateAnimalSalesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import br.gov.pr.idr.infra.shared.support.AuthenticationSupport;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("properties/{propertyId}/animals/{animalId}/sales")
@RequiredArgsConstructor
@RestController
public class AnimalSalesController {

    private final CreateAnimalSalesUseCase createAnimalSalesUseCase;
    private final UpdateAnimalSalesUseCase updateAnimalSalesUseCase;
    private final DeleteAnimalSalesUseCase deleteAnimalSalesUseCase;
    private final GetAnimalSalesByIdUseCase getAnimalSalesByIdUseCase;
    private final ListAnimalSalesUseCase listAnimalSalesUseCase;

    @PostMapping
    public ResponseEntity<CreateAnimalSalesResponse> create(@PathVariable UUID propertyId,
                                                               @PathVariable UUID animalId,
                                                               final Authentication authentication,
                                                               @RequestBody @Valid
                                                               CreateAnimalSalesRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new CreateAnimalSalesCommand(propertyId, animalId, username,
                request.dateSale(), request.reason(), request.value(), request.destination(), request.weight());
        final var output = createAnimalSalesUseCase.execute(command);
        return ResponseEntity.ok(CreateAnimalSalesResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateAnimalSalesResponse> update(@PathVariable UUID propertyId,
                                                               @PathVariable UUID animalId,
                                                               @PathVariable UUID id,
                                                               final Authentication authentication,
                                                               @RequestBody @Valid
                                                               UpdateAnimalSalesRequest request) {
        final var username = AuthenticationSupport.username(authentication);
        final var command = new UpdateAnimalSalesCommand(propertyId, animalId, id, username,
                request.dateSale(), request.reason(), request.value(), request.destination(), request.weight());
        final var output = updateAnimalSalesUseCase.execute(command);
        return ResponseEntity.ok(UpdateAnimalSalesResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID propertyId,
                                        @PathVariable UUID animalId,
                                        @PathVariable UUID id,
                                        final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        deleteAnimalSalesUseCase.execute(new DeleteAnimalSalesCommand(propertyId, animalId, id,
                username));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAnimalSalesByIdOutput> getById(@PathVariable UUID propertyId,
                                                               @PathVariable UUID animalId,
                                                               @PathVariable UUID id,
                                                               final Authentication authentication) {
        final var username = AuthenticationSupport.username(authentication);
        final var output = getAnimalSalesByIdUseCase.execute(
                new GetAnimalSalesByIdCommand(propertyId, animalId, id, username));
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListAnimalSalesOutput>> search(
            @PathVariable UUID propertyId,
            @PathVariable UUID animalId,
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int perPage,
            @RequestParam(required = false) final String terms,
            @RequestParam(defaultValue = "dateSale") final String sort,
            @RequestParam(defaultValue = "asc") final String direction) {
        final var username = AuthenticationSupport.username(authentication);
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        final var output = listAnimalSalesUseCase.execute(
                new ListAnimalSalesCommand(propertyId, animalId, username, query));
        return ResponseEntity.ok(output);
    }
}
