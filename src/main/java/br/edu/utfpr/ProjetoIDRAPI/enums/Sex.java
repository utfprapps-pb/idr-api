package br.edu.utfpr.ProjetoIDRAPI.enums;

public enum Sex {
    M("Macho"),
    F("Fêmea");

    private final String name;

    Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Sex fromAbbreviation(String abbreviation) {
        for (Sex s : values()) {
            if (s.name().equalsIgnoreCase(abbreviation)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Sexo inválido: " + abbreviation);
    }
}