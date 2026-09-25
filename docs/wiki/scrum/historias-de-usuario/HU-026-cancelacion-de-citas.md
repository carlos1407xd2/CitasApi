---
id: HU-026
tipo: historia-de-usuario
titulo: "Cancelación de citas"
estado: Aprobada
epica: "[[EP-004-ciclo-de-vida-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4"
dependencias:
  - "[[HU-025-mis-citas-y-detalle]]"
---
# HU-026 — Cancelación de citas
**COMO** USER  
**QUIERO** cancelar una cita propia futura no terminal  
**PARA** liberar la franja que ya no usaré.

## Criterios de aceptación
### CA-01 — Cancelación transaccional
**Dado** una cita propia, futura y no terminal, **cuando** la cancelo, **entonces** pasa a `CANCELLED`, libera slots y registra historial en una transacción.
### CA-02 — Rechazo de transición inválida
**Dado** una cita ajena, pasada o terminal, **cuando** intento cancelarla, **entonces** no se modifica.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
