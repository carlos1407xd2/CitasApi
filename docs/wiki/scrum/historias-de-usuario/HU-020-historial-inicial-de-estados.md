---
id: HU-020
tipo: historia-de-usuario
titulo: "Historial inicial de estados"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-016-reserva-de-cita-general]]"
  - "[[HU-017-solicitud-de-cita-especializada]]"
---
# HU-020 — Historial inicial de estados
**COMO** equipo de operación  
**QUIERO** conservar la fuente y actor de cada transición  
**PARA** auditar decisiones de reserva.

## Criterios de aceptación
### CA-01 — Historia inmutable
**Dado** una transición de cita, **cuando** se confirma, **entonces** persiste estado, actor cuando exista, fuente, fecha y motivo opcional.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
