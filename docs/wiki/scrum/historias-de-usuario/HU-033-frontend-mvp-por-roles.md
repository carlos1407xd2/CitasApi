---
id: HU-033
tipo: historia-de-usuario
titulo: "Frontend MVP por roles"
estado: Bloqueada
epica: "[[EP-005-experiencia-web]]"
esfuerzo: Muy alto
sprint_sugerido: "Incremento 6"
dependencias:
  - "[[HU-007-frontend-base-aprobado]]"
  - "[[HU-025-mis-citas-y-detalle]]"
  - "[[HU-032-auditoria-y-eventos]]"
---
# HU-033 — Frontend MVP por roles
**COMO** USER, PROFESSIONAL o ADMIN  
**QUIERO** utilizar las pantallas obligatorias conectadas a REST según mi rol  
**PARA** completar el MVP sin datos simulados.

## Criterios de aceptación
### CA-01 — Flujos reales
**Dado** una sesión por rol, **cuando** navego sus capacidades obligatorias, **entonces** la UI usa REST directo, estados loading/vacío/error/éxito y rutas autorizadas.
### CA-02 — Sin mocks
**Dado** una pantalla funcional, **cuando** muestra datos o una confirmación, **entonces** proviene de API y no de `mockData`, `localStorage` ni valores no soportados por PRD.

## Definition of Done
- [ ] Diseño aprobado disponible y fidelidad preservada.
- [ ] Vitest, lint y build pasan; recorrido Docker cross-repo documentado.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Bloqueada | [[HU-007-frontend-base-aprobado]] | No hay diseño aprobado ni proyecto importado apto. |
| CA-02 | No cumple | `portal-de-citas.zip` | El artefacto contiene `mockData.ts`. |
