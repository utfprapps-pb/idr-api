package br.gov.pr.idr.legacy.entity.pregnancydiagnose;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface PregnancyDiagnoseService extends CrudService<PregnancyDiagnose, Long> {
    boolean saveListPregnancyDiagnoses(List<PregnancyDiagnose> pregnancyDiagnoseList);

}
