package br.gov.pr.idr.legacy.search.request;

import br.gov.pr.idr.legacy.search.request.filter.SearchFilter;
import br.gov.pr.idr.legacy.search.request.order.SearchSort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @Builder.Default
    private List<@Valid SearchFilter> filters = new ArrayList<>();
    
    private SearchSort sort;
    
    @Builder.Default
    @Min(0)
    private Integer page = 0;
    
    @Builder.Default
    @Min(1)
    private Integer rows = 50;

}
