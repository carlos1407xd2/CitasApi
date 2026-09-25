---
id: HU-009
tipo: historia-de-usuario
titulo: "Perfil de usuario"
estado: Aprobada
epica: "[[EP-002-catalogos-y-oferta]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 3"
dependencias:
  - "[[HU-006-login-y-sesion-jwt]]"
---
# HU-009 — Perfil de usuario
**COMO** USER autenticado  
**QUIERO** consultar mi perfil y actualizar solo mi teléfono  
**PARA** mantener el dato permitido al día.

## Criterios de aceptación
### CA-01 — Consulta propia
**Dado** una sesión válida, **cuando** consulto `/api/v1/users/me`, **entonces** solo recibo mi perfil.
### CA-02 — Campo permitido
**Dado** mi perfil, **cuando** hago PATCH, **entonces** solo se admite el teléfono y se rechazan cambios a identidad, email y roles.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
