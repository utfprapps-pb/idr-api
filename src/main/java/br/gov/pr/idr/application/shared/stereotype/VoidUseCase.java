package br.gov.pr.idr.application.shared.stereotype;

public abstract class VoidUseCase<I> {

    public abstract void execute(final I command);
}
