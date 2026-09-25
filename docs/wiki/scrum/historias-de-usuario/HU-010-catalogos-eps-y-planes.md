---
id: HU-010
tipo: historia-de-usuario
titulo: "Administración lógica de EPS y planes"
estado: Aprobada
epica: "[[EP-002-catalogos-y-oferta]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 3"
dependencias:
  - "[[HU-003-catalogos-de-solo-lectura]]"
---
# HU-010 — Administración lógica de EPS y planes
**COMO** ADMIN  
**QUIERO** gestionar EPS y planes sin borrado físico  
**PARA** mantener catálogos referenciados íntegros.

## Criterios de aceptación
### CA-01 — Gestión protegida
**Dado** un ADMIN, **cuando** gestiona EPS o planes, **entonces** puede crear, consultar y actualizar con autorización.
### CA-02 — Integridad histórica
**Dado** un catálogo referenciado, **cuando** deja de estar disponible, **entonces** se desactiva en lugar de borrarse.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
