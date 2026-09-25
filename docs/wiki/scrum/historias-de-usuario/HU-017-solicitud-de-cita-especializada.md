---
id: HU-017
tipo: historia-de-usuario
titulo: "Solicitud de cita especializada"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-015-consulta-de-disponibilidad]]"
---
# HU-017 — Solicitud de cita especializada
**COMO** USER  
**QUIERO** solicitar una cita especializada disponible  
**PARA** que ADMIN tome una decisión posterior.

## Criterios de aceptación
### CA-01 — Solicitud retenida
**Dado** slots especializados libres, **cuando** creo la solicitud, **entonces** queda `REQUESTED`, retiene los slots y crea historial con fuente `USER`.
### CA-02 — Selección válida
**Dado** profesional, sede o especialidad no elegibles, **cuando** intento solicitar, **entonces** la API no crea cita ni retiene slots.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
