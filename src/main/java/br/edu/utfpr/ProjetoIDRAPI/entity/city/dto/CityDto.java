package br.edu.utfpr.ProjetoIDRAPI.entity.city.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.region.dto.RegionDto;
import br.edu.utfpr.ProjetoIDRAPI.enums.State;
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
