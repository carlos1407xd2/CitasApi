---
id: HU-003
tipo: historia-de-usuario
titulo: "Catálogos de solo lectura"
estado: Aprobada
epica: "[[EP-002-catalogos-y-oferta]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-002-bootstrap-backend]]"
---
# HU-003 — Catálogos de solo lectura
**COMO** consumidor autorizado de la API  
**QUIERO** consultar sedes, roles, regímenes, estados y planes activos  
**PARA** usar valores canónicos del laboratorio.

## Criterios de aceptación
### CA-01 — Catálogos publicados
**Dado** un catálogo fijo o plan activo, **cuando** se consulta la API, **entonces** se devuelve desde la base canónica sin datos simulados.
### CA-02 — Filtro de planes
**Dado** un plan inactivo, **cuando** se consulta el catálogo para registro, **entonces** no se ofrece como opción.

## Definition of Done
- [ ] Rutas, DTOs, autorizaciones y errores documentados.
- [ ] Pruebas REST relevantes pasan.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
