package br.gov.pr.idr.legacy.search.request;

import br.gov.pr.idr.legacy.search.request.filter.SearchFilter;
import br.gov.pr.idr.legacy.search.request.order.SearchSort;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    private List<SearchFilter> filters = new ArrayList<>();
    private SearchSort sort;
    private Integer page = 0;
    private Integer rows = 50;

}
