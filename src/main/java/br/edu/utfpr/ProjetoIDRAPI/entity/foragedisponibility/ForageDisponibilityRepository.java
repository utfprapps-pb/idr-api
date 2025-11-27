package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.QueryHints; // Import necessário
import jakarta.persistence.QueryHint; // Import necessário

import java.util.List;
import java.util.Optional;

public interface ForageDisponibilityRepository extends
        JpaRepository<ForageDisponibility, Long>,
        JpaSpecificationExecutor<ForageDisponibility> {

    @Query("SELECT fd FROM ForageDisponibility fd JOIN FETCH fd.property WHERE fd.property.id = :propertyId")
    List<ForageDisponibility> findByPropertyIdWithProperty(@Param("propertyId") Long propertyId);

    List<ForageDisponibility> findByProperty_Id(Long propertyId);

    Optional<ForageDisponibility> findByIdAndPropertyId(Long id, Long propertyId);

    @Query("SELECT f FROM ForageDisponibility f WHERE f.id = :id")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "false")})
    Optional<ForageDisponibility> findByIdNoCache(@Param("id") Long id);
}