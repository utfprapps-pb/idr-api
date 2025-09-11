package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForageSearchRequest {
    private String cultivation;
    private Integer page;
    private Integer size;
}

