package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.product.create.CreateProductCommand;
import br.gov.pr.idr.application.inventory_resource.product.create.CreateProductUseCase;
import br.gov.pr.idr.application.inventory_resource.product.delete.DeleteProductUseCase;
import br.gov.pr.idr.application.inventory_resource.product.retrieve.list.ListProductUseCase;
import br.gov.pr.idr.application.inventory_resource.product.update.UpdateProductCommand;
import br.gov.pr.idr.application.inventory_resource.product.update.UpdateProductUseCase;
import br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.product.models.CreateProductRequest;
import br.gov.pr.idr.infra.inventory_resource.product.models.CreateProductResponse;
import br.gov.pr.idr.infra.inventory_resource.product.models.update.UpdateProductRequest;
import br.gov.pr.idr.infra.inventory_resource.product.models.update.UpdateProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("products")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ListProductUseCase listProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    @PostMapping
    public ResponseEntity<CreateProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        final var command = CreateProductCommand.from(request.name(), request.description(),
                request.applicationWay(), request.activePrincipleId(), request.categoryId());
        final var output = createProductUseCase.execute(command);
        return ResponseEntity.ok(CreateProductResponse.from(output));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProductResponse> update(@PathVariable UUID id,
                                                          @RequestBody @Valid UpdateProductRequest request) {
        final var command = UpdateProductCommand.from(id, request.name(), request.description(),
                request.applicationWay(), request.activePrincipleId(), request.categoryId());
        final var output = updateProductUseCase.execute(command);
        return ResponseEntity.ok(UpdateProductResponse.from(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Pagination<ListProductQueryResult>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "") String terms,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        final var query = SearchQuery.from(page, perPage, terms, sort, direction);
        return ResponseEntity.ok(listProductUseCase.execute(query));
    }
}
