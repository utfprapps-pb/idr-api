package br.edu.utfpr.ProjetoIDRAPI.entity.animalpurchases;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface AnimalPurchasesService extends CrudService<AnimalPurchases, Long> {
    boolean saveListPurchases(List<AnimalPurchases> purchases);

}
