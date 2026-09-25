---
id: HU-023
tipo: historia-de-usuario
titulo: "Calidad y hooks S3"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-019-prevencion-de-doble-reserva]]"
---
# HU-023 — Calidad y hooks S3
**COMO** equipo de desarrollo  
**QUIERO** verificaciones locales versionadas antes de cambios  
**PARA** detectar pruebas fallidas y secretos ficticios.

## Criterios de aceptación
### CA-01 — Verificaciones por repositorio
**Dado** un cambio staged, **cuando** se ejecuta el hook, **entonces** backend corre pruebas y detector de secretos; frontend corre lint, tests, build y detector de secretos.
### CA-02 — Evidencia de controles
**Dado** un secreto ficticio o una prueba intencionalmente roja, **cuando** se demuestra el control, **entonces** hay evidencia FAIL y posterior PASS/Green.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe configuración de hooks. |
| CA-02 | Pendiente | — | No existe evidencia. |
