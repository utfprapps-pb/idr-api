package br.gov.pr.idr.legacy.entity.animalpurchases;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface AnimalPurchasesService extends CrudService<AnimalPurchases, Long> {
    boolean saveListPurchases(List<AnimalPurchases> purchases);

}
