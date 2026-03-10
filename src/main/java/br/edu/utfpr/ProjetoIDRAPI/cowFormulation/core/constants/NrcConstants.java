package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants;

import java.math.BigDecimal;

public final class NrcConstants {
    private NrcConstants() {
    }

    // --- Constantes de Energia (Energy) ---
    public static final class Energy {
        public static final BigDecimal TDN_TO_DE_FACTOR = new BigDecimal("0.04409");
        public static final BigDecimal DE_TO_ME_SLOPE = new BigDecimal("1.01");
        public static final BigDecimal DE_TO_ME_INTERCEPT = new BigDecimal("0.45");
        public static final BigDecimal NDT_TO_NEL_SLOPE = new BigDecimal("0.0245");
        public static final BigDecimal NDT_TO_NEL_INTERCEPT = new BigDecimal("0.12");
    }

    // --- Constantes de Proteína (Protein) ---
    public static final class Protein {
        public static final BigDecimal PM_TO_MILK_EFFICIENCY = new BigDecimal("0.67");

        // Perdas Fisiológicas (Descamação e Urina - C91/C92)
        public static final BigDecimal SCURF_FACTOR = new BigDecimal("0.3");
        public static final BigDecimal SCURF_EXPONENT = new BigDecimal("0.6");
        public static final BigDecimal URINE_FACTOR = new BigDecimal("4.1");
        public static final BigDecimal URINE_EXPONENT = new BigDecimal("0.5");

        // Perda Fecal (C93)
        public static final BigDecimal FECAL_IMS_FACTOR = new BigDecimal("30.0"); // 30g por kg de IMS
        public static final BigDecimal PNDR_TO_MCP_EFFICIENCY = new BigDecimal("1.18"); // Divisor na fórmula C93

        // Síntese Microbiana
        public static final BigDecimal RDP_DIGESTIBILITY = new BigDecimal("0.80");
        public static final BigDecimal MCP_CONVERSION = new BigDecimal("0.64");
    }

    // --- Constantes de Gestação (Gestation) ---
    // Adicionado baseado na fórmula C58 e C91
    public static final class Gestation {
        public static final int CONCEPTUS_THRESHOLD_DAYS = 190; // Começa a contar feto a partir daqui
        public static final BigDecimal FETAL_GROWTH_RATE = new BigDecimal("0.06275");
    }

    // --- Constantes de Consumo (Intake) ---
    // Adicionado coeficientes de C21, C22, C23, C24, C26, C27
    public static final class Intake {
        // Correção de Estresse Térmico
        public static final BigDecimal HEAT_STRESS_THRESHOLD = new BigDecimal("20.0");
        public static final BigDecimal HEAT_STRESS_COEFF = new BigDecimal("0.005922");

        // Novilhas (C21/C22) - Fórmula: 1.71 - 0.69 * exp(...)
        public static final BigDecimal HEIFER_INTERCEPT = new BigDecimal("1.71");
        public static final BigDecimal HEIFER_EXP_COEFF = new BigDecimal("0.69");
        public static final BigDecimal HEIFER_EXP_RATE = new BigDecimal("0.35");

        // Vacas Secas (C23/C24) - Fórmula: 1.97 - 0.75 * exp(...)
        public static final BigDecimal DRY_COW_INTERCEPT = new BigDecimal("1.97");
        public static final BigDecimal DRY_COW_EXP_COEFF = new BigDecimal("0.75");
        public static final BigDecimal DRY_COW_EXP_RATE = new BigDecimal("0.16");

        // Vacas em Lactação (C26/C27)
        public static final BigDecimal LACT_FCM_COEFF = new BigDecimal("0.372");  // Leite corrigido
        public static final BigDecimal LACT_WEIGHT_COEFF = new BigDecimal("0.0968");
        public static final BigDecimal LACT_WEIGHT_EXP = new BigDecimal("0.75");
        public static final BigDecimal LAG_EXP_RATE = new BigDecimal("-0.192"); // Taxa de atraso (Lag)
        public static final BigDecimal LAG_OFFSET = new BigDecimal("3.67");
    }

    // --- Constantes de PDR (Regressão Múltipla) ---
    public static final class Rdp {
        public static final BigDecimal BASE_INTERCEPT = new BigDecimal("0.241");
        public static final BigDecimal FACTOR_A = new BigDecimal("0.98");
        public static final BigDecimal FACTOR_B = new BigDecimal("0.977");

        // Termo A (Perdas/Demandas Negativas)
        public static final BigDecimal A_INTERCEPT = new BigDecimal("-1.67");
        public static final BigDecimal A_WEIGHT_COEFF = new BigDecimal("0.00513");
        public static final BigDecimal A_MILK_COEFF = new BigDecimal("0.13");
        public static final BigDecimal A_FAT_COEFF = new BigDecimal("0.597");
        public static final BigDecimal A_PROTEIN_COEFF_1 = new BigDecimal("2.53");

        // Termo B (Demandas Positivas)
        public static final BigDecimal B_INTERCEPT = new BigDecimal("8.83");
        public static final BigDecimal B_WEIGHT_COEFF = new BigDecimal("0.00483");
        public static final BigDecimal B_PROTEIN_COEFF_2 = new BigDecimal("0.119");
        public static final BigDecimal B_FAT_COEFF = new BigDecimal("0.706");
        public static final BigDecimal B_PROTEIN_COEFF_3 = new BigDecimal("2.54");
    }

    public static final class Production {
        public static final int PEAK_MILK_START_DAY = 19;
        public static final int PEAK_MILK_END_DAY = 41;
        public static final BigDecimal PEAK_MILK_BONUS = new BigDecimal("3.0");
    }

    // --- Constantes de Fibra (Fiber) ---
    public static final class Fiber {
        // Exigência mínima recomendada de FDNe para vacas em lactação (Referência NRC)
        // Isso servirá para comparar se a dieta formulada está adequada.
        public static final BigDecimal MIN_PENDF_LACTATION_PCT = new BigDecimal("20.0"); // 20% da MS
    }
}