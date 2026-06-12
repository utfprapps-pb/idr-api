package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.city.create.CreateCityCommand;
import br.gov.pr.idr.application.property_management.city.create.CreateCityUseCase;
import br.gov.pr.idr.infra.property_management.city.models.CreateCityRequest;
import br.gov.pr.idr.infra.property_management.city.models.CreateCityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/city")
@RequiredArgsConstructor
@RestController
public class CityController {

   private final CreateCityUseCase createCityUseCase;

   @PostMapping
   public ResponseEntity<CreateCityResponse> create(@RequestBody @Valid CreateCityRequest request) {
      final var command = CreateCityCommand.from(request.name(), request.state(), request.regionId());
      final var output = createCityUseCase.execute(command);
      return ResponseEntity.ok(CreateCityResponse.from(output));
   }

}
