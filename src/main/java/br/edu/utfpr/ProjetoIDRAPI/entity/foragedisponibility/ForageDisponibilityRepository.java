package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ForageDisponibilityRepository extends
        JpaRepository<ForageDisponibility, Long>,
        JpaSpecificationExecutor<ForageDisponibility> {

    @Query("SELECT fd FROM ForageDisponibility fd JOIN FETCH fd.property WHERE fd.property.id = :propertyId")
    List<ForageDisponibility> findByPropertyIdWithProperty(@Param("propertyId") Long propertyId);

    List<ForageDisponibility> findByProperty_Id(Long propertyId);

}
