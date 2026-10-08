package br.gov.pr.idr.domain.livestock.mastitis;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;

public class Mastitis extends AggregateRoot<MastitisID> {

    private final AnimalID animalId;
    private LocalDate diagnoseDate;
    private String mastitisType;
    private String cmtResult;
    private String ad;
    private String ae;
    private String pd;
    private String pe;

    protected Mastitis(final MastitisID id, final AnimalID animalId, final LocalDate diagnoseDate,
                        final String mastitisType, final String cmtResult, final String ad, final String ae,
                        final String pd, final String pe) {
        super(id);
        this.animalId = animalId;
        this.diagnoseDate = diagnoseDate;
        this.mastitisType = mastitisType;
        this.cmtResult = cmtResult;
        this.ad = ad;
        this.ae = ae;
        this.pd = pd;
        this.pe = pe;
    }

    public static Mastitis with(final MastitisID id, final AnimalID animalId, final LocalDate diagnoseDate,
                                 final String mastitisType, final String cmtResult, final String ad,
                                 final String ae, final String pd, final String pe) {
        return new Mastitis(id, animalId, diagnoseDate, mastitisType, cmtResult, ad, ae, pd, pe);
    }

    public static Mastitis create(final AnimalID animalId, final LocalDate diagnoseDate, final String mastitisType,
                                   final String cmtResult, final String ad, final String ae, final String pd,
                                   final String pe) {
        final var mastitis = new Mastitis(MastitisID.unique(), animalId, diagnoseDate, mastitisType, cmtResult, ad,
                ae, pd, pe);
        mastitis.selfValidate();
        return mastitis;
    }

    public Mastitis update(final LocalDate diagnoseDate, final String mastitisType, final String cmtResult,
                            final String ad, final String ae, final String pd, final String pe) {
        this.diagnoseDate = diagnoseDate;
        this.mastitisType = mastitisType;
        this.cmtResult = cmtResult;
        this.ad = ad;
        this.ae = ae;
        this.pd = pd;
        this.pe = pe;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal da mastite é obrigatório"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public LocalDate getDiagnoseDate() {
        return diagnoseDate;
    }

    public String getMastitisType() {
        return mastitisType;
    }

    public String getCmtResult() {
        return cmtResult;
    }

    public String getAd() {
        return ad;
    }

    public String getAe() {
        return ae;
    }

    public String getPd() {
        return pd;
    }

    public String getPe() {
        return pe;
    }
}
