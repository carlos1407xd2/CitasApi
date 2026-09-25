---
id: HU-004
tipo: historia-de-usuario
titulo: "Contrato REST versionado"
estado: Aprobada
epica: "[[EP-002-catalogos-y-oferta]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-002-bootstrap-backend]]"
relacionadas:
  - "[[HU-003-catalogos-de-solo-lectura]]"
---
# HU-004 — Contrato REST versionado
**COMO** equipo frontend y backend  
**QUIERO** un contrato REST documentado y compatible  
**PARA** integrar ambos repositorios con errores previsibles.

## Criterios de aceptación
### CA-01 — Formatos de agenda
**Dado** una operación de agenda, **cuando** intercambia fecha u hora, **entonces** usa `YYYY-MM-DD`, `HH:mm` y zona de negocio `America/Bogota`.
### CA-02 — Errores consistentes
**Dado** una solicitud inválida, no autenticada, no autorizada, inexistente o en conflicto, **cuando** responde la API, **entonces** documenta y devuelve `400`, `401`, `403`, `404` o `409` según corresponda.

## Definition of Done
- [ ] Plan de cambio cross-repo, compatibilidad y pruebas documentado.
- [ ] Cliente REST y pruebas frontend alineados tras disponer de diseño aprobado.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe contrato implementado. |
| CA-02 | Pendiente | — | No existe contrato implementado. |
