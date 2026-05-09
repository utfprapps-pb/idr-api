package br.gov.pr.idr.legacy.search;

import br.gov.pr.idr.legacy.search.request.SearchRequest;
import br.gov.pr.idr.legacy.search.request.filter.SearchFilter;
import br.gov.pr.idr.legacy.search.request.filter.SearchFilterAdapter;
import br.gov.pr.idr.legacy.search.request.order.SearchSortAdapter;
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
        
        Specification<E> combinedSpec = specifications.stream()
                .reduce(Specification::and)
                .orElse((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        return executor.findAll(combinedSpec, pageable);
    }

    public List<Specification<E>> createSpecifications(List<SearchFilter> filters) {
        if (filters == null) {
            return List.of();
        }
        return filters.stream()
                .map(this::buildSpecification)
                .collect(Collectors.toList());
    }

    private Specification<E> buildSpecification(SearchFilter filter) {
        return (root, query, criteriaBuilder) -> searchFilterAdapter.adapt(filter, root, query, criteriaBuilder);
    }
}
