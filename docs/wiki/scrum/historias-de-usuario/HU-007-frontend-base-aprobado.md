---
id: HU-007
tipo: historia-de-usuario
titulo: "Frontend base aprobado"
estado: Bloqueada
epica: "[[EP-005-experiencia-web]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1"
dependencias: []
---
# HU-007 — Frontend base aprobado
**COMO** usuario del laboratorio  
**QUIERO** una interfaz importada desde un diseño Stitch aprobado  
**PARA** usar los flujos sin perder fidelidad visual.

## Criterios de aceptación
### CA-01 — Referencia visual aprobada
**Dado** el frontend importado, **cuando** se reconcilia código, **entonces** existe evidencia del diseño Stitch aprobado que funciona como fuente de verdad.
### CA-02 — Cliente directo
**Dado** el frontend, **cuando** consume datos, **entonces** llama directamente a `citas-api` sin Express/BFF.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Bloqueada | `citas-web/portal-de-citas.zip` | El ZIP declara AI Studio, pero no acredita aprobación de Stitch. |
| CA-02 | No cumple | `citas-web/portal-de-citas.zip:package.json` | Incluye Express, incompatible con la restricción. |
