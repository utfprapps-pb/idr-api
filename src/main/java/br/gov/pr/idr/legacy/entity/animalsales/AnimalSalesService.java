package br.gov.pr.idr.legacy.entity.animalsales;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface AnimalSalesService extends CrudService<AnimalSales, Long> {
    boolean saveListAnimalSales(List<AnimalSales> animalSales);

}
