---
id: HU-016
tipo: historia-de-usuario
titulo: "Reserva de cita general"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-015-consulta-de-disponibilidad]]"
---
# HU-016 — Reserva de cita general
**COMO** USER  
**QUIERO** confirmar una cita de medicina general disponible  
**PARA** obtener aprobación inmediata.

## Criterios de aceptación
### CA-01 — Aprobación automática
**Dado** una selección general todavía libre, **cuando** la confirmo, **entonces** se crea `APPROVED`, asigna slots e historial con fuente `SYSTEM`.
### CA-02 — Conflicto
**Dado** una selección que deja de estar disponible, **cuando** la confirmo, **entonces** recibo `409 Conflict`.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
