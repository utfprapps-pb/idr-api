package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.exception;

public class NutritionCalculationException extends RuntimeException {
    public NutritionCalculationException(String message) {
        super(message);
    }

    public NutritionCalculationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Ex: Dados insuficientes para calcular Lactação
    public static NutritionCalculationException insufficientData(String variableName) {
        return new NutritionCalculationException("Dados insuficientes para cálculo: " + variableName);
    }
}
