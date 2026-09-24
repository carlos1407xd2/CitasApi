---
id: HU-011
tipo: historia-de-usuario
titulo: "Afiliación inicial opcional"
estado: En validación
epica: "[[EP-002-catalogos-y-oferta]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-003-catalogos-de-solo-lectura]]"
  - "[[HU-005-registro-user]]"
---
# HU-011 — Afiliación inicial opcional
**COMO** visitante  
**QUIERO** elegir opcionalmente un plan activo al registrarme  
**PARA** crear una afiliación inicial sin afectar el flujo de agenda.

## Alcance
- El frontend carga planes activos.
- `insurancePlanId` es opcional.
- La API valida que el plan exista y esté activo y crea la afiliación por FK.
- Si se omite, la reserva posterior conserva afiliación `null`.

## Fuera de alcance
- CRUD ADMIN de EPS/planes en S3 y perfil de afiliación.

## Criterios de aceptación
### CA-01 — Omitir plan
**Dado** un registro válido sin plan, **cuando** se procesa, **entonces** se crea USER sin afiliación.
### CA-02 — Plan activo
**Dado** un `insurancePlanId` activo, **cuando** se registra el USER, **entonces** se crea una afiliación mediante FK sin copiar nombres de EPS o plan a `users`.
### CA-03 — Plan inválido
**Dado** un plan inexistente o inactivo, **cuando** se registra el USER, **entonces** se devuelve un error controlado.

## Definition of Done
- [ ] Request/response y política de `membership_number` aprobados.
- [ ] Pruebas backend y frontend relacionadas pasan.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Cumple backend | `RegisterUserService`, validación MySQL | Registro sin plan devolvió 201 y no creó afiliación. |
| CA-02 | Cumple backend | `JpaUserRegistrationAdapter`, validación MySQL | Plan activo creó una FK y `membership_number` sintético; `users` no guarda nombres de catálogo. |
| CA-03 | Cumple backend | `mvn test`, validación manual | Plan inexistente devolvió 400; prueba unitaria cubre plan inactivo. |
| DoD-01 | Cumple | [[DEC-001-afiliacion-inicial-opcional]] | Política y contrato aprobados. |
| DoD-02 | Cumple | `citas-web/src/lib/api.test.ts`, `npm test`, `npm run lint`, `npm run build` | El cliente carga catálogo activo, registra con/sin plan y sus pruebas pasan. |

## Historial de validación

- 2026-09-24 — Cliente REST reconciliado contra el diseño AI Studio aprobado; queda pendiente revisión independiente para cierre.
