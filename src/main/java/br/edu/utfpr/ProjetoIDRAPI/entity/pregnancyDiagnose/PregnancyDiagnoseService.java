package br.edu.utfpr.ProjetoIDRAPI.entity.pregnancyDiagnose;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.pregnancyDiagnose.PregnancyDiagnose;

import java.util.List;

public interface PregnancyDiagnoseService extends CrudService<PregnancyDiagnose, Long> {
    boolean saveListPregnancyDiagnoses(List<PregnancyDiagnose> pregnancyDiagnoseList);

}
