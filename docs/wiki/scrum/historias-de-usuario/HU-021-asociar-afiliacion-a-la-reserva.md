---
id: HU-021
tipo: historia-de-usuario
titulo: "Asociar afiliación a la reserva"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-011-afiliacion-inicial-opcional]]"
---
# HU-021 — Asociar afiliación a la reserva
**COMO** USER  
**QUIERO** que la reserva conserve mi afiliación actual cuando exista  
**PARA** mantener trazabilidad sin condicionar el agendamiento.

## Criterios de aceptación
### CA-01 — Asociación opcional
**Dado** una reserva válida, **cuando** USER tiene afiliación actual, **entonces** se referencia; si no la tiene, el valor queda `null` y la reserva sigue siendo válida.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
