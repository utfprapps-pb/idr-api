package br.edu.utfpr.ProjetoIDRAPI.search.request.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class SearchFilter {

    public enum Type {
        EQUALS, NOT_EQUALS, LIKE, NOT_LIKE, GREATER, LESS, GREATER_EQUALS, LESS_EQUALS, IN, NOT_IN, IS_NULL, IS_NOT_NULL, BETWEEN
    }

    private String field;
    private Object value;
    private Type type;
}
