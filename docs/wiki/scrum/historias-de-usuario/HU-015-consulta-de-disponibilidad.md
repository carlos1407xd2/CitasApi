---
id: HU-015
tipo: historia-de-usuario
titulo: "Consulta de disponibilidad"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-014-bloques-de-disponibilidad]]"
---
# HU-015 — Consulta de disponibilidad
**COMO** USER  
**QUIERO** filtrar disponibilidad por sede, especialidad, profesional y fecha  
**PARA** elegir una franja reservable.

## Criterios de aceptación
### CA-01 — Oferta elegible
**Dado** filtros válidos, **cuando** consulto disponibilidad, **entonces** solo veo profesionales activos, asociados a la especialidad y habilitados en la sede.
### CA-02 — Duración completa
**Dado** una especialidad de 60 minutos, **cuando** consulto horarios, **entonces** se muestran únicamente inicios con dos slots consecutivos libres.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
