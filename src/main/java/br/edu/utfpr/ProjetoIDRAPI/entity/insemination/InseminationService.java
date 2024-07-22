package br.edu.utfpr.ProjetoIDRAPI.entity.insemination;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.insemination.Insemination;

import java.util.List;

public interface InseminationService extends CrudService<Insemination, Long> {
    boolean saveListInseminations(List<Insemination> inseminations);
}
