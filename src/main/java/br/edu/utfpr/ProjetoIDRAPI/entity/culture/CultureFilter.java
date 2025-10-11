package br.edu.utfpr.ProjetoIDRAPI.entity.culture;

import br.edu.utfpr.ProjetoIDRAPI.enums.CultureType;
import lombok.Data;


@Data
public class CultureFilter {
    private String cultureName;   // filtro por nome
    private CultureType cultureType; // filtro por tipo

}
