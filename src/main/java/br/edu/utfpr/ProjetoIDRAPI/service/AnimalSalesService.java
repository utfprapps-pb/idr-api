package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalSales;

import java.util.List;

public interface AnimalSalesService extends CrudService<AnimalSales, Long> {
    boolean saveListAnimalSales(List<AnimalSales> animalSales);

}
