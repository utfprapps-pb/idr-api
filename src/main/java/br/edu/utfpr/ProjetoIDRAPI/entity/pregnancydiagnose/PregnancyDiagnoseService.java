package br.edu.utfpr.ProjetoIDRAPI.entity.pregnancydiagnose;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface PregnancyDiagnoseService extends CrudService<PregnancyDiagnose, Long> {
    boolean saveListPregnancyDiagnoses(List<PregnancyDiagnose> pregnancyDiagnoseList);

}
