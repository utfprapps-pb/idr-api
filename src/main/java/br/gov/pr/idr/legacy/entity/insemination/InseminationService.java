package br.gov.pr.idr.legacy.entity.insemination;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface InseminationService extends CrudService<Insemination, Long> {
    boolean saveListInseminations(List<Insemination> inseminations);
}
