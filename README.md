# IDR API

## 🏗️ Arquitetura

Este projeto segue **Clean Architecture (Robert C. Martin)**, organizada por **Contextos Delimitados (Bounded Contexts)** e modelada com conceitos táticos de **DDD (Domain-Driven Design)**.

A estrutura em três camadas concêntricas — `domain` → `application` → `infra` — segue a **Dependency Rule**: uma camada só pode depender de camadas mais internas, nunca o contrário. `domain` é o núcleo (Entities, na nomenclatura de Uncle Bob); `application` são os Use Cases; `infra` são os Interface Adapters / Frameworks & Drivers. O vocabulário de **Portas e Adaptadores** (Hexagonal, Cockburn) é usado dentro dessa estrutura — as interfaces do `domain` (`Gateway`) são as portas, e suas implementações em `infra` são os adaptadores — mas a divisão estrita em três camadas concêntricas, com regra de dependência unidirecional validada por teste, é característica de Clean Architecture, não de Hexagonal "pura" (que normalmente trata domínio+aplicação como uma única região simétrica). Por isso preferimos chamar o estilo de **"Clean Architecture com Ports & Adapters"**.

O objetivo é isolar as regras de negócio de frameworks e detalhes técnicos, permitindo que o domínio evolua de forma independente da infraestrutura (banco de dados, web, etc.) e facilitando a manutenção a longo prazo.

A aderência às regras descritas abaixo é validada automaticamente pela suíte de testes arquiteturais em `src/test/java/br/gov/pr/idr/architecture/ArchitectureTest.java`, escrita com [ArchUnit](https://www.archunit.org/). **Qualquer código novo deve passar por esses testes.** Se uma regra precisar mudar, a mudança deve ser discutida e refletida tanto no código quanto neste README.

```
./mvnw test -Dtest=ArchitectureTest
```

---

## 📂 Estrutura de Camadas (Padrão por Contexto)

Cada contexto delimitado vive sob `br.gov.pr.idr.{domain,application,infra}.<contexto>` e segue sempre a mesma estrutura de três camadas:

```
br.gov.pr.idr
├── domain          # Núcleo do negócio — sem dependência de frameworks
│   └── <contexto>/<subdominio>/
├── application     # Casos de uso — orquestram o domínio
│   └── <contexto>/<subdominio>/<operacao>/
└── infra           # Adaptadores — Web, JPA, e-mail, etc.
    └── <contexto>/<subdominio>/{api, persistence, ...}
```

### `domain/`
Modelos de domínio (`Entity`, `AggregateRoot`, `ValueObject`), Identificadores, Gateways (portas de saída) e regras de negócio. **Não pode depender de Spring, JPA/Hibernate, da camada `application`, `infra` ou `legacy`.**

### `application/`
Casos de uso (Use Cases) que orquestram o domínio para atender uma operação específica (ex.: `create`, `update`, `delete`, `retrieve/get`, `retrieve/search`). Não conhece detalhes de transporte (Spring Web) nem de persistência (JPA/Hibernate) — comunica-se com o mundo externo apenas através das interfaces (`Gateway`) declaradas no `domain`. **Não pode depender de `infra` nem de `legacy`.**

### `infra/`
Adaptadores de entrada (`api/` — Controllers REST) e de saída (`persistence/` — JPA, gateways de e-mail, etc.). É a única camada que pode conhecer Spring, JPA/Hibernate e qualquer biblioteca de framework. **Não pode depender de `legacy`.**

### `legacy/`
Código anterior à migração para a arquitetura hexagonal, mantido apenas até que seus contextos sejam totalmente migrados. Nenhuma camada nova (`domain`, `application`, `infra`) pode depender dele.

---

## 🗺️ Mapeamento de Contextos Delimitados

| Contexto | Responsabilidade | Entidades |
|---|---|---|
| `iam` | Identidade, segurança e acesso | `User`, `Permission`, `RefreshToken`, `PasswordRecovery`, `EmailGateway` |
| `property_management` | Propriedade rural, produtor e localização | `Property`, `PropertyCollaborator`, `Producer`, `City`, `Region`, sincronização (`sync`) |
| `livestock` *(a migrar)* | Manejo, vida e saúde animal | `Animal`, `Breed`, `AnimalDiseases`, `Insemination`, `PregnancyDiagnose`, `Mastitis`, `Disease` |
| `crop_management` *(a migrar)* | Plantio, manejo agrícola e pragas | `Culture`, `GeneralCultivations`, `ForageDisponibility`, `VegetableDisease`, `Plague` |
| `inventory_resource` *(a migrar)* | Insumos e recursos produtivos | `Product`, `ProductCategory`, `Medication`, `ActivePrinciple`, `LandProduct` |

Os contextos marcados como *(a migrar)* ainda residem em `legacy/entity/...` e devem ser portados seguindo o guia abaixo.

---

## 🧩 Conceitos de DDD (`domain.shared`)

O `domain` não é só um conjunto de POJOs — ele é modelado com os **building blocks táticos do Domain-Driven Design**. Os contratos abaixo (`Entity`, `AggregateRoot`, `Identifier`, `@ValueObject`) ficam em `domain.shared` e são a base de qualquer modelo novo.

### Entity

> *"Uma entidade é definida pela definição de determinadas características: deve ter um identificador único; controle de estado e seus comportamentos são designados a partir de um agregado."* — javadoc de [`Entity`](src/main/java/br/gov/pr/idr/domain/shared/Entity.java)

- Todo objeto com identidade própria (ciclo de vida, igualdade por ID) estende `Entity<I extends Identifier>`.
- Toda `Entity` implementa `validate(ValidationHandler handler)` com suas invariantes, e chama `selfValidate()` no construtor/nos métodos de mutação — garantindo que o objeto **nunca exista em estado inválido** (princípio "Always-Valid Domain Model"). Ex.: `PropertyCollaborator.validate(...)`.
- Uma entidade que não é a raiz do agregado (ex.: `PropertyCollaborator` dentro de `Property`) não deve ser persistida/alterada isoladamente — seu ciclo de vida é controlado pelo agregado a que pertence.

### Aggregate Root

> *"Utilize um agregado quando: existe um identificador único; o próprio agregado faz o controle do seu estado; agregados podem manipular estados de outras entidades; relacionamento entre agregados é realizado via Identificador."* — javadoc de [`AggregateRoot`](src/main/java/br/gov/pr/idr/domain/shared/AggregateRoot.java)

- `AggregateRoot<I extends Identifier>` estende `Entity` e marca o **único ponto de entrada** para mutações de um agregado (ex.: `Property` é a raiz, e controla a lista de `PropertyCollaborator`).
- Um agregado define um **limite de consistência transacional**: tudo que está dentro dele é alterado atomicamente através da raiz.
- Agregados **nunca referenciam outro agregado diretamente** — a relação é sempre feita pelo Identificador (`ProducerID`, `CityID`, `UserID` em `Property`, em vez de `Producer`/`City`/`User` inteiros). Isso evita grafos de objetos gigantes e mantém agregados pequenos e independentes para concorrência/transação.
- Cada Use Case carrega e persiste **um agregado raiz por vez** via seu `Gateway`.

### Value Object

> *"Considere utilizar ValueObject quando: seu próprio valor é seu identificador único; seu valor é imutável; um V.O deve se autovalidar; boa prática: utilize records para representar ValueObjects."* — javadoc de [`@ValueObject`](src/main/java/br/gov/pr/idr/domain/shared/ValueObject.java)

- Objetos sem identidade própria, comparados por valor (`equals`/`hashCode` estrutural) — ex.: `Coord` (`latitude`/`longitude`), `CPF`.
- Devem ser **imutáveis** e residir em um pacote `..domain..vo..`, anotados com `@ValueObject`.
- Devem **se autovalidar** na própria construção (ex.: `Coord` lança `DomainException` no construtor canônico do `record` se `latitude`/`longitude` forem nulos) — nunca dependem de validação externa.
- Implementação preferencial: `record` (imutabilidade e `equals`/`hashCode` de graça); alternativas aceitas são classes `final` ou `enum`.

### Identifier

- Contrato (`UUID id()`) para identificadores de entidades/agregados. Cada agregado/entidade tem seu próprio tipo forte (`PropertyID`, `ProducerID`, `UserID`, ...) em vez de `UUID` cru — evita trocar acidentalmente o ID de um tipo por outro e torna explícita a referência entre agregados.

### Outros blocos de apoio

| Classe/Pacote | Uso |
|---|---|
| `DomainEventPublisher` | Porta para publicação de eventos de domínio, implementada na infra (`SpringDomainEventPublisher`). |
| `domain.shared.exceptions` | Exceções de domínio — toda classe deve terminar com `Exception` (ex.: `NotFoundException`, `NotificationException`, `DomainException`). |
| `domain.shared.validation` | `ValidationHandler`, `NotificationValidation`, `DomainError` — mecanismo de notificação de erros (**Notification Pattern**), que acumula todas as violações de uma vez em vez de interromper a validação no primeiro `throw`. |

---

## ⚙️ Casos de Uso (`application`)

- Toda classe concreta em `application` deve terminar com **`UseCase`**, **`Command`** ou **`Output`**.
- Todo `UseCase` concreto deve estender `UseCase<I, O>` (retorna valor) ou `VoidUseCase<I>` (sem retorno).
- Todo `UseCase` concreto deve ser anotado com:
  - `@CommandUseCase` — operações de escrita. Aplica `@Transactional` e `@Service` automaticamente.
  - `@QueryUseCase` — operações de leitura. Aplica `@Transactional(readOnly = true)` e `@Service` automaticamente.
- Um Use Case deve conter **regras de integração** entre entidades, agregados e outros bounded contexts (ex.: validar se um `City` ou `Producer` existe antes de criar uma `Property`). **Regras que pertencem à entidade não devem "vaiar" para o Use Case** — se a regra é sobre a consistência interna do agregado, ela pertence ao `domain`.
- Cada operação tem seu próprio subpacote (`create`, `update`, `delete`, `retrieve/get`, `retrieve/search`, ...), contendo o `UseCase`, seu `Command` (entrada) e seu `Output` (saída).

Exemplo (`application/property_management/property/create`):

```java
@CommandUseCase
public class CreatePropertyUseCase extends UseCase<CreatePropertyCommand, CreatePropertyOutput> {
    // depende apenas de Gateways (interfaces do domain)
}
```

---

## 🔌 Gateways (Portas de Saída)

- Toda interface declarada em `domain` representa uma porta de saída e deve terminar com **`Gateway`** (ex.: `PropertyGateway`, `EmailGateway`).
- Toda implementação concreta de um `Gateway` é um adaptador e deve residir em `infra` (ex.: `PropertyPostgresGateway`, `ThymeleafEmailGateway`).
- O `application` depende apenas da interface; o Spring injeta a implementação correta em tempo de execução (Inversão de Dependência).

---

## 🗄️ Persistência (JPA)

- Toda classe anotada com `@Entity` deve:
  - Residir em um pacote `..infra..persistence..`.
  - Ter o nome terminado em **`JPAEntity`** (ex.: `PropertyJPAEntity`).
- Entidades JPA são *modelos de persistência*, não modelos de domínio — a conversão entre `XxxJPAEntity` e a entidade de domínio (`Xxx`) é responsabilidade do Gateway (ou de um mapper dedicado, ex.: MapStruct).
- Use os campos padrão de versionamento/auditoria nas entidades JPA (versão otimista, `createdAt`, `updatedAt`, usuário responsável) usando as anotações JPA padrão.

## 🌐 Controllers REST

- Toda classe anotada com `@RestController` deve:
  - Residir em um pacote `..infra..api..`.
  - Ter o nome terminado em **`Controller`** (ex.: `PasswordRecoveryController`).
- Controllers recebem/retornam `record`s (`XxxRequest` / `XxxResponse`), convertendo para `Command`/`Output` da camada `application`. Não devem conter regra de negócio.

---

## 🚫 Isolamento do Domínio

A camada `domain` é o núcleo da aplicação e deve ser totalmente agnóstica a frameworks:

- ❌ Não pode usar `@Service`, `@Component`, `@Repository` ou `@Autowired` do Spring.
- ❌ Não pode depender de nenhum pacote `org.springframework..`.
- ❌ Não pode usar `@Entity`, `@Table` ou depender de `jakarta.persistence..` / `org.hibernate..`.
- ❌ Não pode depender de `application`, `infra` ou `legacy`.
- ✅ Injeção de dependência via construtor, sem anotações.

A camada `application` também é isolada de detalhes técnicos:

- ❌ Não pode depender de `org.springframework.web..` / `org.springframework.http..` (é agnóstica ao protocolo de transporte).
- ❌ Não pode depender de `jakarta.persistence..` / `org.hibernate..` (é agnóstica ao banco de dados).
- ❌ Não pode depender de `infra` ou `legacy`.

---

## ⚠️ Exceções de Domínio

Classes em pacotes `..domain..exceptions..` devem terminar com **`Exception`** (ex.: `NotFoundException`).

---

## 🚀 Guia de Migração (Best Practices)

Ao migrar uma entidade de `legacy` para a arquitetura hexagonal:

1. **Domínio primeiro:** crie o modelo puro em `domain`, estendendo `Entity` ou `AggregateRoot`, com métodos estáticos de fábrica (`create`, `with`) — *Effective Java*. Implemente `validate(ValidationHandler)` com as invariantes do objeto.
2. **Identificador e Value Objects:** crie um `XxxID` implementando `Identifier`. Atributos que são valor-puro (CPF, coordenadas, etc.) devem ser `record`s anotados com `@ValueObject` em um pacote `vo`.
3. **Gateway:** declare a porta de saída em `domain` (`XxxGateway`) com os métodos que o Use Case precisa (`save`, `findById`, `existsById`, ...).
4. **Use Case:** implemente em `application/<contexto>/<entidade>/<operacao>`, anotado com `@CommandUseCase` ou `@QueryUseCase`, estendendo `UseCase`/`VoidUseCase`. Use apenas Gateways como dependência.
5. **DTOs:** utilize `record`s para `Command`/`Output` (em `application`) e para `Request`/`Response` (em `infra`).
6. **Adaptadores:** implemente o Gateway em `infra` (ex.: `XxxPostgresGateway`), a entidade JPA (`XxxJPAEntity`) e o `Controller` REST.
7. **Desacoplamento:** a camada `application` não deve conhecer JPA ou Controller — comunica-se apenas através de Portas (Gateways) definidas no `domain`.
8. **Valide com os testes arquiteturais:** rode `ArchitectureTest` antes de abrir o PR.

---

![img.png](assets/img.png)
