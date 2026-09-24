# DEC-001 — Afiliación inicial opcional

## DECISIÓN

El alcance S3 establece que el registro recibe `insurancePlanId` opcional y,
si el plan está activo, crea una afiliación mediante FK. El esquema canónico
requiere además `membership_number NOT NULL` en
`user_insurance_affiliations`. El usuario autorizó generar en backend un
identificador sintético de laboratorio, sin añadir un campo al formulario ni
modificar la tabla canónica.

## Impacto

- Bloquea la implementación de HU-005 y HU-011.
- Bloquea definir de forma final el request/response de registro.
- No bloquea el bootstrap técnico, la especificación ni las funciones ajenas
  al registro.

## Contrato adoptado

- `insurancePlanId` permanece opcional en `POST /api/v1/auth/register`.
- Si existe y está activo, backend crea la afiliación actual por FK y genera
  `membership_number` sintético con prefijo `AF-LAB-`.
- Si se omite, no se crea afiliación.
- No se persisten nombres de EPS, plan o régimen en `users`.
