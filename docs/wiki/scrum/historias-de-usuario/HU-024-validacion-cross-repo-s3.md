---
id: HU-024
tipo: historia-de-usuario
titulo: "Validación cross-repo S3"
estado: Bloqueada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-033-frontend-mvp-por-roles]]"
---
# HU-024 — Validación cross-repo S3
**COMO** equipo del laboratorio  
**QUIERO** verificar citas generales y especializadas de extremo a extremo  
**PARA** asegurar que el contrato REST funciona entre ambos repositorios.

## Criterios de aceptación
### CA-01 — Recorrido completo
**Dado** Docker y ambas aplicaciones, **cuando** se reserva una cita general y una especializada, **entonces** la UI muestra la respuesta real `APPROVED` o `REQUESTED` y la persistencia coincide.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Bloqueada | [[HU-007-frontend-base-aprobado]] | Falta diseño aprobado e importación válida del frontend. |
