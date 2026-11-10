package br.gov.pr.idr.infra.shared.support;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public final class PaginationFactory {

    private PaginationFactory() {}

    public static <E, T> Pagination<T> from(final Page<E> page, final Function<E, T> mapper) {
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.map(mapper).toList());
    }
}
