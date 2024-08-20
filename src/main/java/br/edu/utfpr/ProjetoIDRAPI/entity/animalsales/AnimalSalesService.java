package br.edu.utfpr.ProjetoIDRAPI.entity.animalsales;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface AnimalSalesService extends CrudService<AnimalSales, Long> {
    boolean saveListAnimalSales(List<AnimalSales> animalSales);

}
