---
id: HU-008
tipo: historia-de-usuario
titulo: "Recuperación de contraseña"
estado: Aprobada
epica: "[[EP-001-fundacion-y-seguridad]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 3"
dependencias:
  - "[[HU-006-login-y-sesion-jwt]]"
---
# HU-008 — Recuperación de contraseña
**COMO** usuario  
**QUIERO** recuperar mi contraseña con un token temporal de un solo uso  
**PARA** recuperar acceso sin exponer mi cuenta.

## Criterios de aceptación
### CA-01 — Solicitud no enumerativa
**Dado** cualquier email, **cuando** solicito recuperación, **entonces** recibo `202` genérico.
### CA-02 — Restablecimiento seguro
**Dado** un token válido sin usar, **cuando** cambio la contraseña, **entonces** el token se consume, la contraseña se hashea y se revocan los refresh tokens.

## Definition of Done
- [ ] Token persistido únicamente como hash, temporal y de un uso.
- [ ] Buzón local solo ADMIN y perfil `local`, sin persistir token claro.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
