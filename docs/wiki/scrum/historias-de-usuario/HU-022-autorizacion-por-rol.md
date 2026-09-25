---
id: HU-022
tipo: historia-de-usuario
titulo: "Autorización por rol y ownership"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-006-login-y-sesion-jwt]]"
---
# HU-022 — Autorización por rol y ownership
**COMO** actor autenticado  
**QUIERO** acceder únicamente a mis capacidades y datos autorizados  
**PARA** proteger las operaciones del laboratorio.

## Criterios de aceptación
### CA-01 — Roles protegidos
**Dado** una ruta de ADMIN, PROFESSIONAL o USER, **cuando** un rol no permitido la invoca, **entonces** recibe `403`.
### CA-02 — Ownership
**Dado** un recurso propio, **cuando** otro actor intenta gestionarlo, **entonces** no obtiene acceso.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
