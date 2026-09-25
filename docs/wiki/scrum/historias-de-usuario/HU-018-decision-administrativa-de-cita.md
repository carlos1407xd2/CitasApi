---
id: HU-018
tipo: historia-de-usuario
titulo: "Decisión administrativa de cita"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-017-solicitud-de-cita-especializada]]"
---
# HU-018 — Decisión administrativa de cita
**COMO** ADMIN  
**QUIERO** aprobar o rechazar solicitudes especializadas pendientes  
**PARA** decidir su atención conservando la consistencia de slots.

## Criterios de aceptación
### CA-01 — Aprobación
**Dado** una solicitud `REQUESTED`, **cuando** ADMIN aprueba, **entonces** pasa a `APPROVED` y conserva sus slots.
### CA-02 — Rechazo
**Dado** una solicitud `REQUESTED`, **cuando** ADMIN rechaza con motivo, **entonces** pasa a `REJECTED`, registra historial y libera slots.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
