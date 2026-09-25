---
id: HU-001
tipo: historia-de-usuario
titulo: "Modelo 3FN y migraciones"
estado: En desarrollo
epica: "[[EP-001-fundacion-y-seguridad]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1"
dependencias: []
---
# HU-001 — Modelo 3FN y migraciones
**COMO** equipo del laboratorio  
**QUIERO** disponer de un esquema 3FN versionado y compatible con la base existente  
**PARA** preservar integridad y trazabilidad del dominio.

## Alcance
- Justificación 1FN–3FN, claves, cardinalidades, índices y migraciones Flyway posteriores a la base existente.

## Criterios de aceptación
### CA-01 — Normalización verificable
**Dado** el modelo del PRD, **cuando** se inspecciona el esquema, **entonces** EPS, plan, régimen, especialidad y estados no se duplican transitivamente en usuarios o citas.
### CA-02 — Migración compatible
**Dado** una base creada desde el esquema canónico, **cuando** se añade un cambio, **entonces** este usa una migración Flyway posterior sin reescribir la versión inicial.

## Definition of Done
- [ ] Diagrama, dependencias funcionales y comparación con referencia disponibles.
- [ ] Pruebas de persistencia relevantes pasan.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | `database/reference/db.sql` | Esquema de referencia disponible; no existe implementación propia. |
| CA-02 | Pendiente | — | No existe `pom.xml` ni migraciones Flyway. |
