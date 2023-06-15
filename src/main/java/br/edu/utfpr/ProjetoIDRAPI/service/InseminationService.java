package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Insemination;

import java.util.List;

public interface InseminationService extends CrudService<Insemination, Long> {
    boolean saveListInseminations(List<Insemination> inseminations);
}
