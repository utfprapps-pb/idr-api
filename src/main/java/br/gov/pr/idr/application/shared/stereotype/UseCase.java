package br.gov.pr.idr.application.shared.stereotype;

public abstract class UseCase<I, O> {

    public abstract O execute(final I command);
}
