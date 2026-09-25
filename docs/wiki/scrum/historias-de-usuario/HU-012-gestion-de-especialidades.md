---
id: HU-012
tipo: historia-de-usuario
titulo: "Gestión de especialidades"
estado: Aprobada
epica: "[[EP-002-catalogos-y-oferta]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-006-login-y-sesion-jwt]]"
---
# HU-012 — Gestión de especialidades
**COMO** ADMIN  
**QUIERO** gestionar especialidades activas de 30 o 60 minutos  
**PARA** configurar la duración de la atención.

## Criterios de aceptación
### CA-01 — Duraciones válidas
**Dado** una especialidad, **cuando** ADMIN la crea o actualiza, **entonces** solo acepta 30 o 60 minutos.
### CA-02 — Baja lógica
**Dado** una especialidad referenciada, **cuando** deja de ofrecerse, **entonces** puede desactivarse sin borrado físico.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
