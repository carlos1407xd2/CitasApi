---
id: HU-030
tipo: historia-de-usuario
titulo: "Cierre de atención"
estado: Aprobada
epica: "[[EP-006-operacion-y-automatizacion]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 5"
dependencias:
  - "[[HU-029-agenda-del-profesional]]"
---
# HU-030 — Cierre de atención
**COMO** PROFESSIONAL  
**QUIERO** marcar una atención propia pasada como completada o no asistida  
**PARA** cerrar el ciclo operativo.

## Criterios de aceptación
### CA-01 — Cierre válido
**Dado** una cita propia `APPROVED` cuyo fin ya ocurrió, **cuando** elijo `COMPLETED` o `NO_SHOW`, **entonces** cambia de estado y deja historial.
### CA-02 — Cierre inválido
**Dado** una cita ajena, no aprobada o no terminada, **cuando** intento cerrarla, **entonces** no se modifica.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
