---
id: HU-006
tipo: historia-de-usuario
titulo: "Login y sesión JWT"
estado: Aprobada
epica: "[[EP-001-fundacion-y-seguridad]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1"
dependencias:
  - "[[HU-005-registro-user]]"
---
# HU-006 — Login y sesión JWT
**COMO** usuario registrado  
**QUIERO** iniciar, renovar y cerrar una sesión segura  
**PARA** acceder solo a las capacidades de mi rol.

## Criterios de aceptación
### CA-01 — Inicio de sesión
**Dado** credenciales válidas, **cuando** inicio sesión, **entonces** recibo un access token de corta duración y refresh token separado.
### CA-02 — Control de sesión
**Dado** un refresh válido, **cuando** se renueva o revoca, **entonces** se respeta su expiración y revocación sin almacenar el valor en claro.

## Definition of Done
- [ ] Autorización por rol y pruebas de casos positivos/negativos.
- [ ] Secretos configurados únicamente por entorno.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
