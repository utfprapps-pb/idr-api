package br.edu.utfpr.ProjetoIDRAPI.entity.region.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {

    private Long id;

    @NotNull
    private String name;
}
