---
id: HU-028
tipo: historia-de-usuario
titulo: "Decisión de reprogramación"
estado: Aprobada
epica: "[[EP-004-ciclo-de-vida-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4"
dependencias:
  - "[[HU-027-solicitud-de-reprogramacion]]"
---
# HU-028 — Decisión de reprogramación
**COMO** ADMIN  
**QUIERO** aprobar o rechazar una reprogramación pendiente  
**PARA** resolver la nueva franja sin perder consistencia.

## Criterios de aceptación
### CA-01 — Aprobación
**Dado** una solicitud `PENDING`, **cuando** ADMIN aprueba, **entonces** intercambia franjas atómicamente y registra el historial aplicable.
### CA-02 — Rechazo
**Dado** una solicitud `PENDING`, **cuando** ADMIN rechaza con motivo, **entonces** libera la franja nueva y conserva la original.

## Evidence de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
