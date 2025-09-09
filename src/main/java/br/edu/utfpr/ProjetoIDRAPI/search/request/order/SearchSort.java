package br.edu.utfpr.ProjetoIDRAPI.search.request.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class SearchSort {

    public enum Type {
        ASC, DESC
    }

    private String field;
    private Type type;
}
