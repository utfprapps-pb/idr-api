# 🐄 IDR API - Motor de Formulação de Gado Leiteiro (NRC 2001)
Este módulo é responsável pela engenharia reversa e implementação das regras de negócio de nutrição (Excel $\to$ Java API), baseado nas normas do NRC 2001.

---

## 🏗 Arquitetura do Projeto
O projeto segue uma arquitetura modular para facilitar o desenvolvimento paralelo entre os 3 programadores, isolando Regras de Negócio (Core), Motor de Cálculo (Engine) e Implementações Específicas (Features).

---

## Estrutura de Pastas
```plaintext
br.edu.utfpr.ProjetoIDRAPI.cowFormulation

├── core                            # [ZONA INTOCÁVEL] Base estável do sistema
│   ├── constants                   # NrcConstants.java (Todos os coeficientes do Excel)
│   ├── domain                      # Entidades: AnimalContext, DietIngredient, Enums
│   └── service                     # Interfaces Base (Contracts)
│
├── engine                          # [MOTOR] Serviços compartilhados
│   └── intake                      # DryMatterIntakeCalculator (Cálculo de IMS - C21 a C27)
│
└── modules                         # [ZONA DE DESENVOLVIMENTO] Implementação dos nutrientes
├── energy                      # 👷‍♀️ P1 - Dayse (Energia)
├── protein                     # 👷‍♂️ P2 & P3 - Proteína
│   ├── fractions               # 👷‍♂️ P2 - André (Frações: PDR, PNDR)
│   └── requirements            # 👷‍♂️ P3 - Lucas (Exigência Final: PM)
└── fiber                       # 👷‍♂️ P3 - Lucas (Dieta e Fibra: FDNe)
```

---

## 👥 Distribuição de Tarefas

### 👷‍♀️ P1: Dayse (Time Energia)
- **Responsabilidade:** Implementar os cálculos de Energia.
- **Diretório de Trabalho:** `modules/energy`
- **Classes a criar:** `MetabolizableEnergyCalculator.java` (EM - Energia Metabolizável), `NetEnergyCalculator.java` (EL - Energia Líquida)
- **O que já está pronto para uso:** 
  - Injeção do `DryMatterIntakeCalculator`. 
  - Constantes de conversão em `NrcConstants.Energy` (NDT $\to$ ED $\to$ EM $\to$ EL).

### 👷‍♂️ P2: André (Time Frações Proteicas)
- **Responsabilidade:** Calcular as frações proteicas básicas que alimentam o sistema.
- **Diretório de Trabalho:** `modules/protein/fractions`
- **Classes a criar:**
  - `RdpRequirementCalculator.java` (PDR - Proteína Degradável no Rúmen)
  - `PndrRequirementCalculator.java` (PNDR - Proteína Não Degradável)
- **Atenção:**
  - Utilizar `NrcConstants.Rdp` para as equações de regressão múltipla (Termos A e B).
  - Implementar a interface `PndrServiceInterface` para que o Lucas (P3) possa consumir seu cálculo.

### 👷‍♂️ P3: Lucas (Time Integração & Fibra)
- **Responsabilidade:** Fechar o cálculo de Proteína Metabolizável (que depende do André) e calcular Fibra da Dieta.
- **Diretório de Trabalho:** `modules/protein/requirements` e `modules/fiber`.
- **Classes:**
  - `MetabolizableProteinCalculator.java` (PM - Já implementado com Mock).
  - `EffectiveFiberCalculator.java` (FDNe - Oferta da Dieta).

---

## 🧩 O que já está pronto (Core & Engine)
Usem as classes abaixo que já estão implementadas para facilitar o desenvolvimento.

### 1. **`AnimalContext` (O Estado do Animal)**
Encapsula toda a lógica temporal e de ajuste de peso. **Tentem não fazer cálculos de datas nos serviços, usem os métodos abaixo:**
   - `getDaysInMilk()`: Dias em lactação (DEL) reais.
   - `getDaysPregnant()`: Dias de gestação reais.
   - `getProjectedBodyWeight()`: Peso futuro baseado na meta de ganho/perda (C82).
   - `getConceptusWeight()`: Peso do feto para descontar do peso vivo (C58).
     - `getFatCorrectedMilk()`: Leite corrigido para gordura 4% (FCM - C16).
   - `getPeakAdjustedMilkYield()`: Leite com bônus de pico (C9).

### 2. **`DryMatterIntakeCalculator` (O Motor IMS)**
Calcula a Ingestão de Matéria Seca Predita (C28) baseada em:
   - Novilhas (C21/C22)
   - Vacas Secas (C23/C24)
   - Vacas em Lactação (C26/C27)
   - Ajuste de Estresse Térmico (>20°C)..

Como usar: Basta injetar `@RequiredArgsConstructor` e chamar `.calculatePredictedIntake(ctx)`.

### 3. **`NrcConstants` (A Fonte da Verdade)**
Todos os números fixos do Excel foram extraídos para esta classe estática.

| Classe Interna | O que contém | Exemplos |
| --- | --- | --- |
| `Energy` | Conversão Energética | "`TDN_TO_DE`, `DE_TO_ME_SLOPE`, `NDT_TO_NEL`" |
| `Protein` | Fatores Fisiológicos | "`SCURF_FACTOR` (Descamação), `URINE_FACTOR`, `MCP_CONVERSION`" |
| `Rdp` | Regressão Múltipla | "Coeficientes para cálculo de PDR (`A_INTERCEPT`, `B_WEIGHT_COEFF`)" |
| `Intake` | Curvas de Ingestão | "Coeficientes exponenciais para Novilhas e Vacas" |
| `Gestation` | Crescimento Fetal | "Taxas de crescimento do feto (`FETAL_GROWTH_RATE`)" |

---

## ⚠️ Regras Técnicas de Desenvolvimento
### 1. Use BigDecimal: Nunca use double para cálculos financeiros ou de precisão acumulativa. Use `MathContext.DECIMAL64` para divisões.
   - *Errado*: `valor * 0.5`
   - *Certo*: `valor.multiply(new BigDecimal("0.5"))`

### 2. Injeção de Dependência: Usem `@Service` e `@RequiredArgsConstructor` (Lombok) para injetar dependências. Evitem `new Classe()`.

### 3. Interfaces: Se seu cálculo depende de um módulo que ainda não está pronto (ex: PM depende de PNDR), crie uma interface e implemente um Mock temporário.

### 4. Zero Hardcoding: Se encontrar um número na fórmula (ex: `0.06275`), não digite no código. Adicione em `NrcConstants`.

---

## 📅 Status de Implementação

| Nutriente | Responsável | Dependência | Status |
| --- | --- | --- | --- |
| **IMS (Intake)** | Lucas/Core | `AnimalContext` | ✅ **Concluído** |
| **EM (Energia)** | Dayse (P1) | `IMS`, `Constantes` | 🟡 A Fazer |
| **EL (Energia)** | Dayse (P1) | `IMS`, `Constantes` | 🟡 A Fazer |
| **PDR** | André (P2) | `IMS`, `Regressão` | 🟡 A Fazer |
| **PNDR** | André (P2) | `IMS` | 🟡 A Fazer |
| **PM** | Lucas (P3) | `PNDR (André)` | 🟠 Mockado (Aguardando P2) |
| **FDNe** | Lucas (P3) | `Dieta` | ✅ **Concluído (Oferta)** |