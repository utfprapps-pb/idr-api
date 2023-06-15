package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalPurchases;

import java.util.List;

public interface AnimalPurchasesService extends CrudService<AnimalPurchases, Long> {
    boolean saveListPurchases(List<AnimalPurchases> purchases);

}
