package br.gov.pr.idr.legacy.entity.city.dto;

import br.gov.pr.idr.legacy.entity.region.dto.RegionDto;
import br.gov.pr.idr.legacy.enums.State;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private RegionDto region;

    private State state;

}
