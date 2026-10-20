package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.breed.create.CreateBreedCommand;
import br.gov.pr.idr.application.livestock.breed.create.CreateBreedUseCase;
import br.gov.pr.idr.application.livestock.breed.delete.DeleteBreedUseCase;
import br.gov.pr.idr.application.livestock.breed.retrieve.list.ListBreedOutput;
import br.gov.pr.idr.application.livestock.breed.retrieve.list.ListBreedUseCase;
import br.gov.pr.idr.application.livestock.breed.update.UpdateBreedCommand;
import br.gov.pr.idr.application.livestock.breed.update.UpdateBreedUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.breed.models.CreateBreedRequest;
import br.gov.pr.idr.infra.livestock.breed.models.CreateBreedResponse;
import br.gov.pr.idr.infra.livestock.breed.models.update.UpdateBreedRequest;
import br.gov.pr.idr.infra.livestock.breed.models.update.UpdateBreedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("breeds")
@RequiredArgsConstructor
@RestController
public class BreedController {

    private final CreateBreedUseCase createBreedUseCase;
    private final UpdateBreedUseCase updateBreedUseCase;
    private final ListBreedUseCase listBreedUseCase;
    private final DeleteBreedUseCase deleteBreedUseCase;

    @PostMapping
    public ResponseEntity<CreateBreedResponse> create(@RequestBody @Valid CreateBreedRequest request) {
        final var command = new CreateBreedCommand(request.breedName());
        final var output = createBreedUseCase.execute(command);
        return ResponseEntity.ok(CreateBreedResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateBreedResponse> update(@PathVariable UUID id,
                                                         @RequestBody @Valid UpdateBreedRequest request) {
        final var command = new UpdateBreedCommand(id, request.breedName());
        final var output = updateBreedUseCase.execute(command);
        return ResponseEntity.ok(UpdateBreedResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteBreedUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListBreedOutput>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "") String terms,
            @RequestParam(defaultValue = "breedName") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return ResponseEntity.ok(listBreedUseCase.execute(query));
    }
}
