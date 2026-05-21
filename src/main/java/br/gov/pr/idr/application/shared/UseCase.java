package br.gov.pr.idr.application.shared;

public abstract class UseCase<I, O> {

    public abstract O execute(final I command);
}
