---
id: HU-013
tipo: historia-de-usuario
titulo: "Gestión de profesionales"
estado: Aprobada
epica: "[[EP-002-catalogos-y-oferta]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-012-gestion-de-especialidades]]"
---
# HU-013 — Gestión de profesionales
**COMO** ADMIN  
**QUIERO** crear y configurar profesionales ficticios  
**PARA** ofrecer disponibilidad por especialidad y sede.

## Criterios de aceptación
### CA-01 — Creación segura
**Dado** datos sintéticos válidos, **cuando** ADMIN crea un profesional, **entonces** se almacena contraseña temporal con BCrypt y nunca se devuelve ni registra.
### CA-02 — Asignaciones
**Dado** un profesional, **cuando** ADMIN lo configura, **entonces** puede asignar especialidades —una primaria—, una o ambas sedes y activarlo/desactivarlo.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
