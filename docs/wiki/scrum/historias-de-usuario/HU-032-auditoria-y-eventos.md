---
id: HU-032
tipo: historia-de-usuario
titulo: "Auditoría y eventos post-commit"
estado: Aprobada
epica: "[[EP-006-operacion-y-automatizacion]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 5"
dependencias:
  - "[[HU-020-historial-inicial-de-estados]]"
---
# HU-032 — Auditoría y eventos post-commit
**COMO** ADMIN y actor autorizado  
**QUIERO** consultar historial de citas y preparar eventos posteriores al commit  
**PARA** auditar cambios y habilitar automatización futura sin PII.

## Criterios de aceptación
### CA-01 — Historial por ownership
**Dado** una cita, **cuando** USER, PROFESSIONAL o ADMIN consulta el historial, **entonces** solo accede según la relación autorizada con la cita.
### CA-02 — Evento seguro
**Dado** una transición confirmada, **cuando** publica `AppointmentStatusChanged`, **entonces** sucede después del commit e incluye identificador, estado anterior/nuevo, fuente, actor y fecha sin PII.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
