package br.gov.pr.idr.infra.inventory_resource.product.persistence;

import br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductJPARepository extends JpaRepository<ProductJPAEntity, UUID> {

    @Query("SELECT new br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult(" +
            "p.id, p.name, p.description, p.applicationWay, ap.id, ap.name, pc.id, pc.description) " +
            "FROM ProductJPAEntity p " +
            "LEFT JOIN ActivePrincipleJPAEntity ap ON p.activePrincipleId = ap.id " +
            "LEFT JOIN ProductCategoryJPAEntity pc ON p.categoryId = pc.id " +
            "WHERE LOWER(p.name) LIKE :pattern ESCAPE '\\'")
    Page<ListProductQueryResult> search(@Param("pattern") String pattern, Pageable pageable);
}
