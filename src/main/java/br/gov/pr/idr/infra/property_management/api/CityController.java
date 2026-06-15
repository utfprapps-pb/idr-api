package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.city.create.CreateCityCommand;
import br.gov.pr.idr.application.property_management.city.create.CreateCityUseCase;
import br.gov.pr.idr.application.property_management.city.retrieve.list.ListCityUseCase;
import br.gov.pr.idr.application.property_management.city.update.UpdateCityCommand;
import br.gov.pr.idr.application.property_management.city.update.UpdateCityUseCase;
import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.city.models.CreateCityRequest;
import br.gov.pr.idr.infra.property_management.city.models.CreateCityResponse;
import br.gov.pr.idr.infra.property_management.city.models.update.UpdateCityRequest;
import br.gov.pr.idr.infra.property_management.city.models.update.UpdateCityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/v1/cities")
@RequiredArgsConstructor
@RestController
public class CityController {

   private final CreateCityUseCase createCityUseCase;
   private final ListCityUseCase listCityUseCase;
   private final UpdateCityUseCase updateCityUseCase;

   @PostMapping
   public ResponseEntity<CreateCityResponse> create(@RequestBody @Valid CreateCityRequest request) {
      final var command = CreateCityCommand.from(request.name(), request.state(), request.regionId());
      final var output = createCityUseCase.execute(command);
      return ResponseEntity.ok(CreateCityResponse.from(output));
   }

   @GetMapping
   public ResponseEntity<Pagination<ListCityQueryResult>> list(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int perPage,
           @RequestParam(defaultValue = "") String terms,
           @RequestParam(defaultValue = "name") String sort,
           @RequestParam(defaultValue = "asc") String direction
   ) {
      final var query = SearchQuery.from(page, perPage, terms, sort, direction);
      return ResponseEntity.ok(listCityUseCase.execute(query));
   }

   @PutMapping("/{id}")
   public ResponseEntity<UpdateCityResponse> update(@PathVariable UUID id,
                                                    @RequestBody @Valid UpdateCityRequest request) {
      final var command = UpdateCityCommand.from(id, request.regionId(), request.name());
      final var output = updateCityUseCase.execute(command);
      return ResponseEntity.ok(UpdateCityResponse.from(output));
   }
}
