package br.edu.utfpr.ProjetoIDRAPI.search;

import br.edu.utfpr.ProjetoIDRAPI.search.request.SearchRequest;
import br.edu.utfpr.ProjetoIDRAPI.search.request.filter.SearchFilter;
import br.edu.utfpr.ProjetoIDRAPI.search.request.filter.SearchFilterAdapter;
import br.edu.utfpr.ProjetoIDRAPI.search.request.order.SearchSortAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchHandler<E> {

    SearchFilterAdapter searchFilterAdapter = new SearchFilterAdapter();
    JpaSpecificationExecutor<E> executor;

    public SearchHandler(JpaSpecificationExecutor<E> executor) {
        Objects.requireNonNull(executor, "A JpaSpecificationExecutor must be provided");
        this.executor = executor;
    }

    public Page<E> handle(SearchRequest searchRequest) {
        List<Specification<E>> specifications = createSpecifications(searchRequest.getFilters());
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getRows(), SearchSortAdapter.adapt(searchRequest.getSort()));
        return executor.findAll(
            Specification.where(specifications.stream().reduce(Specification::and).orElse(null)),
            pageable
        );
    }

    public List<Specification<E>> createSpecifications(List<SearchFilter> filters) {
        return filters.stream()
                .map(this::buildSpecification)
                .collect(Collectors.toList());
    }

    private Specification<E> buildSpecification(SearchFilter filter) {
        return (root, query, criteriaBuilder) -> searchFilterAdapter.adapt(filter, root, query, criteriaBuilder);
    }
}
