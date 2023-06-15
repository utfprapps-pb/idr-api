package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.PregnancyDiagnose;

import java.util.List;

public interface PregnancyDiagnoseService extends CrudService<PregnancyDiagnose, Long> {
    boolean saveListPregnancyDiagnoses(List<PregnancyDiagnose> pregnancyDiagnoseList);

}
