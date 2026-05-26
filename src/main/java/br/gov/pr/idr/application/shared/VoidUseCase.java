package br.gov.pr.idr.application.shared;

public abstract class VoidUseCase<I> {

    public abstract void execute(final I command);
}
