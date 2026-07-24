# Sync offline-first — guia de extensão

Arquitetura genérica de sincronização offline-first. O objetivo de projeto é: **adicionar
uma nova entidade sincronizável = criar classes novas**, sem tocar no orquestrador
(`UploadSyncUseCase` / `DownloadSyncUseCase`) nem no `SyncController` (Open/Closed Principle).

## Peças da arquitetura

| Camada | Componente | Papel |
|---|---|---|
| domain | `OfflineEntityType` | enum dos tipos que trafegam no **upload** |
| domain | `SyncableJPAEntity` (infra `shared/persistence`) | base `@MappedSuperclass` com `version`, `updatedAt`, `deletedAt` |
| domain | `SyncItem`, `SyncEntityResult`, `SyncIdMapping(+Gateway)`, `SyncScopeGateway` | contratos genéricos |
| application (upload) | `SyncEntityHandler`, `SyncContext`, `SyncPayloadReader` | pipeline de upload |
| application (download) | `SyncSnapshotContributor`, `SyncScope`, `DownloadSyncQuery/Output` | pipeline de download |
| infra | handlers, contributors, gateways Postgres | implementações registradas via Spring |

O `UploadSyncUseCase` injeta `List<SyncEntityHandler>` (indexa por tipo, ordena
topologicamente por `dependencies()`); o `DownloadSyncUseCase` injeta
`List<SyncSnapshotContributor>` (agrega por `collectionName()`). Ambos descobrem as novas
implementações automaticamente.

## Passo a passo — nova entidade sincronizável

### 1. Entidade JPA
- Estenda `SyncableJPAEntity` e anote a entidade com `@SQLRestriction("deleted_at is null")`
  (esconde soft-deletes das consultas de negócio).
- O `ddl-auto` materializa `version`/`updated_at`/`deleted_at`; se precisar de índice único
  parcial (`WHERE deleted_at IS NULL`), acrescente-o ao `SyncSchemaInitializer`.

### 2. Soft delete
- No gateway de exclusão, troque `deleteById` por `findById(...).ifPresent(e -> { e.markDeleted(); save(e); })`.

### 3. Upload
- Adicione o valor ao enum `OfflineEntityType`.
- Crie um `@Component implements SyncEntityHandler`:
  - `type()` → o novo `OfflineEntityType`;
  - `dependencies()` → tipos que precisam ser processados antes (ex.: `PROPERTY → {PRODUCER}`);
  - `handle(cmd, ctx)`:
    1. `if (ctx.isAlreadySynced(localId)) return EXISTING` com `ctx.findPersistedServerId(...)`;
    2. leia o payload com `SyncPayloadReader.of(cmd.data())`;
    3. resolva referências a outras entidades do batch via `ctx.resolveLocalMapping(...)`
       (ou `ctx.findPersistedServerId(...)`);
    4. deduplique / persista;
    5. `ctx.persistMapping(localId, serverId, type)` (idempotência, mesma transação).
- Lance `RuntimeException` para falha da entidade — o orquestrador a isola como `FAILED`
  sem abortar o batch.

### 4. Download
- Crie um `@Component implements SyncSnapshotContributor`:
  - `collectionName()` → nome da coleção na resposta (único);
  - `contribute(scope)`:
    - `if (scope.hasNoRegions()) return List.of();`
    - **snapshot** (`!scope.isIncremental()`): registros ativos no escopo → `SyncItem.active(...)`;
    - **delta** (`scope.isIncremental()`): ativos com `updatedAt > since` → `active`,
      **mais** soft-deletados com `deletedAt > since` → `SyncItem.tombstone(...)`.
- Adicione ao repositório:
  - derivado `...AndUpdatedAtAfter(...)` para o delta ativo;
  - `@Query(nativeQuery = true)` `findDeletedSince(...)` para tombstones — **nativo é
    obrigatório**, pois `@SQLRestriction` filtra os excluídos das queries JPQL/derivadas.

### 5. Exposição HTTP da coleção no download
A coleção já aparece em `DownloadSyncOutput.collections`. As coleções legadas
(`regions`, `cities`, `producers`) são mapeadas no `DownloadSyncResponse` no seu shape
histórico por retrocompatibilidade. Para expor uma coleção **nova** via HTTP, adicione um
campo tipado ao `DownloadSyncResponse` **ou** exponha `output.collections()` genericamente.
Este é o único ponto "central" tocado — e apenas para dar forma à resposta, por decisão de
compatibilidade (não afeta orquestração).

## Checklist

- [ ] Entidade estende `SyncableJPAEntity` + `@SQLRestriction("deleted_at is null")`
- [ ] Exclusão via soft delete no gateway
- [ ] (upload) valor em `OfflineEntityType` + `SyncEntityHandler` `@Component`
- [ ] (download) `SyncSnapshotContributor` `@Component` + queries de delta e `findDeletedSince` nativa
- [ ] Índice único parcial no `SyncSchemaInitializer`, se aplicável
- [ ] `UploadSyncUseCase`, `DownloadSyncUseCase` e `SyncController` **não** foram modificados
