# DEC-001 — Afiliación inicial opcional

## PREGUNTA ABIERTA

El alcance S3 establece que el registro recibe `insurancePlanId` opcional y,
si el plan está activo, crea una afiliación mediante FK. El esquema canónico
requiere además `membership_number NOT NULL` en
`user_insurance_affiliations`. No existe fuente aprobada que determine si ese
valor se debe solicitar, generar sintéticamente o modificar mediante una
migración posterior.

## Impacto

- Bloquea la implementación de HU-005 y HU-011.
- Bloquea definir de forma final el request/response de registro.
- No bloquea el bootstrap técnico, la especificación ni las funciones ajenas
  al registro.

## Alternativas que requieren aprobación

1. Incluir `membershipNumber` opcional en el request cuando se seleccione plan.
2. Generar un identificador sintético del laboratorio en backend.
3. Crear una migración posterior que permita el valor nulo.

No se seleccionará una alternativa por inferencia.
