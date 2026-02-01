package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums;


public enum ProductionStage {
    LACTATING("Lactação"),
    DRY("Seca"),
    PRE_FRESH("Pré-Parto"),
    HEIFER("Novilha");

    private final String description;

    ProductionStage(String description) {
        this.description = description;
    }

    // Método utilitário para checar se está em lactação
    public boolean isProducingMilk() {
        return this == LACTATING;
    }
}