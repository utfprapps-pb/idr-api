package br.gov.pr.idr.legacy.entity.pest.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.pest.Pest;
import br.gov.pr.idr.legacy.entity.pest.PestRepository;
import br.gov.pr.idr.legacy.entity.pest.PestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PestServiceImpl extends CrudServiceImpl<Pest, Long> implements PestService {

    private final PestRepository pestRepository;

    @Override
    protected JpaRepository<Pest, Long> getRepository() {
        return this.pestRepository;
    }

    @Override
    public JpaSpecificationExecutor<Pest> getSpecExecutor() {
        return this.pestRepository;
    }
}
