---
titulo: "Índice Scrum — FCV Citas"
---

# Especificación Scrum del sistema de citas

## Estado de la trazabilidad

La especificación inicial se deriva de `PRD.md`, `RESTRICCIONES_TECNICAS.md`,
`database/REQUISITOS_NORMALIZACION_3FN.md` y los alcances S3–S5 aportados por
el usuario el 2026-09-24. Los repositorios no contienen aún evidencia de una
implementación funcional, por lo que ninguna HU se marca como completada.

## Épicas

- [[EP-001-fundacion-y-seguridad]]
- [[EP-002-catalogos-y-oferta]]
- [[EP-003-disponibilidad-y-reservas]]
- [[EP-004-ciclo-de-vida-de-citas]]
- [[EP-005-experiencia-web]]
- [[EP-006-operacion-y-automatizacion]]

## Incrementos sugeridos

1. Fundación: modelo 3FN, backend, autenticación y contrato mínimo.
2. S3: catálogos, oferta, disponibilidad, reservas y calidad.
3. S4 identidad: recuperación, perfil, afiliación y catálogos configurables.
4. S4 ciclo de vida: mis citas, cancelación y reprogramación.
5. S4 operación: agenda profesional, auditoría y puerto de eventos.
6. S5: estado integrado, OpenAPI y webhook n8n.

## Decisiones o incógnitas pendientes

- La fuente disponible no define el contrato HTTP de registro ni la política
  para `membership_number` cuando la afiliación inicial solo recibe
  `insurancePlanId`. [[DEC-001-afiliacion-inicial-opcional]]
- El ZIP de AI Studio no acredita aprobación del diseño Stitch. No se deben
  reconciliar ni alterar pantallas hasta disponer de la referencia aprobada.
- La base del proyecto contiene `docs/wiki/` y el AGENTS raíz establece como
  destino canónico `docs/FCV Dev/`. La Wiki operativa se inicializa en esta
  última ruta; estas especificaciones permanecen en la ruta histórica exigida
  por la skill Scrum.
