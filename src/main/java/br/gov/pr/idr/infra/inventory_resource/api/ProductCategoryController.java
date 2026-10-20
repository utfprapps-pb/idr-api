package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.product_category.create.CreateProductCategoryCommand;
import br.gov.pr.idr.application.inventory_resource.product_category.create.CreateProductCategoryUseCase;
import br.gov.pr.idr.application.inventory_resource.product_category.delete.DeleteProductCategoryUseCase;
import br.gov.pr.idr.application.inventory_resource.product_category.retrieve.list.ListProductCategoryOutput;
import br.gov.pr.idr.application.inventory_resource.product_category.retrieve.list.ListProductCategoryUseCase;
import br.gov.pr.idr.application.inventory_resource.product_category.update.UpdateProductCategoryCommand;
import br.gov.pr.idr.application.inventory_resource.product_category.update.UpdateProductCategoryUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.product_category.models.CreateProductCategoryRequest;
import br.gov.pr.idr.infra.inventory_resource.product_category.models.CreateProductCategoryResponse;
import br.gov.pr.idr.infra.inventory_resource.product_category.models.update.UpdateProductCategoryRequest;
import br.gov.pr.idr.infra.inventory_resource.product_category.models.update.UpdateProductCategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("productCategories")
@RequiredArgsConstructor
@RestController
public class ProductCategoryController {

    private final CreateProductCategoryUseCase createProductCategoryUseCase;
    private final UpdateProductCategoryUseCase updateProductCategoryUseCase;
    private final ListProductCategoryUseCase listProductCategoryUseCase;
    private final DeleteProductCategoryUseCase deleteProductCategoryUseCase;

    @PostMapping
    public ResponseEntity<CreateProductCategoryResponse> create(@RequestBody @Valid CreateProductCategoryRequest request) {
        final var command = new CreateProductCategoryCommand(request.description());
        final var output = createProductCategoryUseCase.execute(command);
        return ResponseEntity.ok(CreateProductCategoryResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProductCategoryResponse> update(@PathVariable UUID id,
                                                                  @RequestBody @Valid UpdateProductCategoryRequest request) {
        final var command = new UpdateProductCategoryCommand(id, request.description());
        final var output = updateProductCategoryUseCase.execute(command);
        return ResponseEntity.ok(UpdateProductCategoryResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteProductCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListProductCategoryOutput>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "") String terms,
            @RequestParam(defaultValue = "description") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return ResponseEntity.ok(listProductCategoryUseCase.execute(query));
    }
}
