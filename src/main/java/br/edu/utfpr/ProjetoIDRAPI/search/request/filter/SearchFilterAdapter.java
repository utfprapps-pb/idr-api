package br.edu.utfpr.ProjetoIDRAPI.search.request.filter;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterAdapter implements Specification {

    private SearchFilter filter;

    public Predicate adapt(SearchFilter filter, Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        this.filter = filter;
        return toPredicate(root, query, criteriaBuilder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
        Expression field = root.get(filter.getField());
        Object value = filter.getValue();
        return switch (filter.getType()) {
            case EQUALS -> cb.equal(field, value);
            case NOT_EQUALS -> cb.notEqual(field, value);
            case LIKE -> cb.like(field, "%" + value + "%");
            case NOT_LIKE -> cb.notLike(field, "%" + value + "%");
            case GREATER -> cb.greaterThan(field, value.toString());
            case LESS -> cb.lessThan(field, value.toString());
            case GREATER_EQUALS -> cb.greaterThanOrEqualTo(field, value.toString());
            case LESS_EQUALS -> cb.lessThanOrEqualTo(field, value.toString());
            case IN -> field.in((List<?>) value);
            case NOT_IN -> cb.not(field.in((List<?>) value));
            case IS_NULL -> cb.isNull(field);
            case IS_NOT_NULL -> cb.isNotNull(field);
            case BETWEEN -> {
                List<?> values = (List<?>) value;
                yield cb.between(
                        field,
                        (Comparable<Object>) values.get(0),
                        (Comparable<Object>) values.get(1)
                );
            }
        };
    }

}