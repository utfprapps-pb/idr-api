package br.gov.pr.idr.infra.property_management.city.models;

import br.gov.pr.idr.domain.property_management.city.vo.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCityRequest(@NotBlank(message = "Nome não pode ser nulo") String name,
                                @NotNull(message = "Estado não pode ser nulo") State state,
                                @NotNull(message = "Região não pode ser nulo") UUID regionId) {
}
