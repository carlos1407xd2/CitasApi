---
id: HU-005
tipo: historia-de-usuario
titulo: "Registro de USER"
estado: En validación
epica: "[[EP-001-fundacion-y-seguridad]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1"
dependencias:
  - "[[HU-002-bootstrap-backend]]"
  - "[[HU-004-contrato-rest]]"
---
# HU-005 — Registro de USER
**COMO** visitante  
**QUIERO** crear una cuenta USER con datos mínimos válidos  
**PARA** autenticarme y solicitar citas.

## Alcance
- Nombres, apellidos, documento, email, teléfono y contraseña; email/documento únicos; hash adaptativo.

## Fuera de alcance
- Recuperación y perfil, que se tratan en HU independientes.

## Criterios de aceptación
### CA-01 — Registro seguro
**Dado** datos válidos y únicos, **cuando** se registra un visitante, **entonces** se crea una cuenta USER con contraseña hasheada.
### CA-02 — Duplicados controlados
**Dado** un email o documento existente, **cuando** se registra un visitante, **entonces** recibe un error de conflicto sin revelar información sensible.

## Definition of Done
- [ ] Contrato de solicitud y respuesta aprobado y documentado.
- [ ] Pruebas de éxito, duplicados y validación pasan.
- [ ] No se registran contraseñas ni tokens.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Cumple | `AuthController`, `RegisterUserService`, `mvn test` | Registro sintético validado contra MySQL con respuesta 201 y BCrypt. |
| CA-02 | Cumple | `JpaUserRegistrationAdapter`, validación manual | Email duplicado respondió 409 controlado. |
| DoD-01 | Cumple | `docs/FCV Dev/llm-wiki/wiki/contrato-rest-s2-s5.md` | Contrato aprobado y documentado. |
| DoD-02 | Cumple | `mvn test` | Cuatro pruebas backend verdes. |

## Historial de validación

- 2026-09-24 — Backend implementado y validado; pendiente revisión independiente para cierre.
