---
id: EP-005
tipo: epica
titulo: "Experiencia web sin mocks"
estado: Bloqueada
historias:
  - "[[HU-007-frontend-base-aprobado]]"
  - "[[HU-033-frontend-mvp-por-roles]]"
dependencias:
  - "[[EP-001-fundacion-y-seguridad]]"
---

# EP-005 — Experiencia web sin mocks

## Objetivo

Integrar la UI aprobada contra REST sin sustituir reglas de negocio del
backend ni alterar el diseño Stitch aprobado.

## Riesgos e incógnitas

- Falta evidencia de diseño Stitch aprobado; el ZIP AI Studio no basta para
  autorizar reconciliación visual según `citas-web/AGENTS.md`.

## Historias de usuario

- [[HU-007-frontend-base-aprobado]]
- [[HU-033-frontend-mvp-por-roles]]
