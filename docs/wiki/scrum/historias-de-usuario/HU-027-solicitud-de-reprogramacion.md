---
id: HU-027
tipo: historia-de-usuario
titulo: "Solicitud de reprogramación"
estado: Aprobada
epica: "[[EP-004-ciclo-de-vida-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4"
dependencias:
  - "[[HU-025-mis-citas-y-detalle]]"
---
# HU-027 — Solicitud de reprogramación
**COMO** USER  
**QUIERO** solicitar una nueva franja para una cita aprobada futura  
**PARA** conservar la original mientras ADMIN decide.

## Criterios de aceptación
### CA-01 — Retención doble segura
**Dado** una cita propia `APPROVED` futura, **cuando** solicito reprogramación válida, **entonces** conserva profesional/especialidad, retiene nueva franja y mantiene la original durante `PENDING`.
### CA-02 — Una pendiente
**Dado** una cita con solicitud `PENDING`, **cuando** intento crear otra, **entonces** se rechaza.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
