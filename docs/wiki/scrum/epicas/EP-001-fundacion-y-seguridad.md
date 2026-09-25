---
id: EP-001
tipo: epica
titulo: "Fundación, identidad y seguridad"
estado: En desarrollo
historias:
  - "[[HU-001-modelo-3fn-y-migraciones]]"
  - "[[HU-002-bootstrap-backend]]"
  - "[[HU-005-registro-user]]"
  - "[[HU-006-login-y-sesion-jwt]]"
  - "[[HU-008-recuperacion-de-contrasena]]"
dependencias: []
---

# EP-001 — Fundación, identidad y seguridad

## Objetivo

Disponer de una base segura y trazable para operar cuentas ficticias del
laboratorio sin duplicar datos de catálogo ni almacenar contraseñas o tokens
en texto claro.

## Actores

- USER
- ADMIN

## Alcance

- Modelo relacional 3FN, Spring Boot, Flyway, registro, sesión JWT y
  recuperación de contraseña.

## Fuera de alcance

- Datos reales, SMTP obligatorio y perfil de afiliación fuera de lo definido.

## Historias de usuario

- [[HU-001-modelo-3fn-y-migraciones]]
- [[HU-002-bootstrap-backend]]
- [[HU-005-registro-user]]
- [[HU-006-login-y-sesion-jwt]]
- [[HU-008-recuperacion-de-contrasena]]

## Riesgos e incógnitas

- El contrato de registro y la afiliación inicial requieren la decisión
  documentada en [[DEC-001-afiliacion-inicial-opcional]].
