package br.gov.pr.idr.legacy.enums;

public enum State {
    AC("Acre"),
    AL("Alagoas"),
    AP("Amapá"),
    AM("Amazonas"),
    BA("Bahia"),
    CE("Ceará"),
    DF("Distrito Federal"),
    ES("Espírito Santo"),
    GO("Goiás"),
    MA("Maranhão"),
    MT("Mato Grosso"),
    MS("Mato Grosso do Sul"),
    MG("Minas Gerais"),
    PA("Pará"),
    PB("Paraíba"),
    PR("Paraná"),
    PE("Pernambuco"),
    PI("Piauí"),
    RJ("Rio de Janeiro"),
    RN("Rio Grande do Norte"),
    RS("Rio Grande do Sul"),
    RO("Rondônia"),
    RR("Roraima"),
    SC("Santa Catarina"),
    SP("São Paulo"),
    SE("Sergipe"),
    TO("Tocantins");

    private final String name;

    State(String nome) {
        this.name = nome;
    }

    public String getNome() {
        return name;
    }

    public static State fromAbbreviation(String abbreviation) {
        for (State s : values()) {
            if (s.name().equalsIgnoreCase(abbreviation)) {
                return s;
            }
        }
        throw new IllegalArgumentException("UF inválida: " + abbreviation);
    }
}