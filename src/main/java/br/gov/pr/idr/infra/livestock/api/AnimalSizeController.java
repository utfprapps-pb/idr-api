package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animalsize.create.CreateAnimalSizeCommand;
import br.gov.pr.idr.application.livestock.animalsize.create.CreateAnimalSizeUseCase;
import br.gov.pr.idr.application.livestock.animalsize.delete.DeleteAnimalSizeUseCase;
import br.gov.pr.idr.application.livestock.animalsize.retrieve.list.ListAnimalSizeOutput;
import br.gov.pr.idr.application.livestock.animalsize.retrieve.list.ListAnimalSizeUseCase;
import br.gov.pr.idr.application.livestock.animalsize.update.UpdateAnimalSizeCommand;
import br.gov.pr.idr.application.livestock.animalsize.update.UpdateAnimalSizeUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animalsize.models.CreateAnimalSizeRequest;
import br.gov.pr.idr.infra.livestock.animalsize.models.CreateAnimalSizeResponse;
import br.gov.pr.idr.infra.livestock.animalsize.models.update.UpdateAnimalSizeRequest;
import br.gov.pr.idr.infra.livestock.animalsize.models.update.UpdateAnimalSizeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("animal-sizes")
@RequiredArgsConstructor
@RestController
public class AnimalSizeController {

    private final CreateAnimalSizeUseCase createAnimalSizeUseCase;
    private final UpdateAnimalSizeUseCase updateAnimalSizeUseCase;
    private final ListAnimalSizeUseCase listAnimalSizeUseCase;
    private final DeleteAnimalSizeUseCase deleteAnimalSizeUseCase;

    @PostMapping
    public ResponseEntity<CreateAnimalSizeResponse> create(@RequestBody @Valid CreateAnimalSizeRequest request) {
        final var command = new CreateAnimalSizeCommand(request.sizeName());
        final var output = createAnimalSizeUseCase.execute(command);
        return ResponseEntity.ok(CreateAnimalSizeResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateAnimalSizeResponse> update(@PathVariable UUID id,
                                                         @RequestBody @Valid UpdateAnimalSizeRequest request) {
        final var command = new UpdateAnimalSizeCommand(id, request.sizeName());
        final var output = updateAnimalSizeUseCase.execute(command);
        return ResponseEntity.ok(UpdateAnimalSizeResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteAnimalSizeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListAnimalSizeOutput>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "") String terms,
            @RequestParam(defaultValue = "sizeName") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return ResponseEntity.ok(listAnimalSizeUseCase.execute(query));
    }
}
