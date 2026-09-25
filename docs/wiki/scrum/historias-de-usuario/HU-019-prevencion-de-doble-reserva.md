---
id: HU-019
tipo: historia-de-usuario
titulo: "Prevención de doble reserva"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-016-reserva-de-cita-general]]"
  - "[[HU-017-solicitud-de-cita-especializada]]"
---
# HU-019 — Prevención de doble reserva
**COMO** usuario del sistema  
**QUIERO** que una franja pueda reservarse solo una vez  
**PARA** confiar en la disponibilidad publicada.

## Criterios de aceptación
### CA-01 — Exclusión transaccional
**Dado** dos reservas concurrentes por los mismos slots, **cuando** se procesan, **entonces** exactamente una es válida y la otra devuelve `409`.
### CA-02 — Integridad posterior
**Dado** el intento concurrente terminado, **cuando** se consulta persistencia, **entonces** no hay dos citas asociadas a los mismos slots.

## Definition of Done
- [ ] Prueba concurrente repetible con un éxito y un conflicto.
- [ ] Verificación independiente Builder/Verifier registrada.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
