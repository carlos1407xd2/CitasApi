---
id: HU-014
tipo: historia-de-usuario
titulo: "Bloques de disponibilidad"
estado: Aprobada
epica: "[[EP-003-disponibilidad-y-reservas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias:
  - "[[HU-013-gestion-de-profesionales]]"
---
# HU-014 — Bloques de disponibilidad
**COMO** PROFESSIONAL  
**QUIERO** crear, consultar, editar y eliminar mis bloques futuros  
**PARA** publicar mi agenda en sedes autorizadas.

## Criterios de aceptación
### CA-01 — Bloque válido
**Dado** un profesional activo y una sede asignada, **cuando** crea un bloque futuro en límites de 30 minutos, **entonces** se generan slots atómicos de 30 minutos.
### CA-02 — Reglas de protección
**Dado** un bloque inválido, solapado o comprometido, **cuando** se intenta crear, editar o borrar, **entonces** la API lo rechaza sin afectar slots existentes.

## Definition of Done
- [ ] Pruebas de futuro, solapamiento, sede y edición/eliminación con slots comprometidos pasan.
- [ ] Authorization/ownership de PROFESSIONAL comprobada.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
