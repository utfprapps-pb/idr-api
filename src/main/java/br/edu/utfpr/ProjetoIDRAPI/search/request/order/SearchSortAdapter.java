package br.edu.utfpr.ProjetoIDRAPI.search.request.order;

import org.springframework.data.domain.Sort;

public class SearchSortAdapter {

    public static Sort adapt(SearchSort sort) {
        if (sort == null) {
            return defaultSort();
        }
        return byType(sort.getType(), sort.getField());
    }

    /**
     * Provides a default sort method. Curretly unsorted.
     * Could provide a default sort by id if needed.
     * @return Sort object - unsorted
     */
    public static Sort defaultSort() {
        return Sort.unsorted();
    }

    public static Sort byType(SearchSort.Type type, String field) {
        return switch (type) {
            case ASC -> Sort.by(Sort.Order.asc(field));
            case DESC -> Sort.by(Sort.Order.desc(field));
        };
    }

}
