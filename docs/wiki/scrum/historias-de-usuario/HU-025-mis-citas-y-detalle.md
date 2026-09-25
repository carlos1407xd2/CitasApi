---
id: HU-025
tipo: historia-de-usuario
titulo: "Mis citas y detalle"
estado: Aprobada
epica: "[[EP-004-ciclo-de-vida-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4"
dependencias:
  - "[[HU-016-reserva-de-cita-general]]"
---
# HU-025 — Mis citas y detalle
**COMO** USER  
**QUIERO** consultar y filtrar mis citas, incluido el detalle  
**PARA** conocer fecha, sede, profesional, especialidad, duración, estado y motivos aplicables.

## Criterios de aceptación
### CA-01 — Listado propio
**Dado** citas del USER, **cuando** consulta por estado o rango, **entonces** solo recibe las propias con la información mínima definida.
### CA-02 — Detalle protegido
**Dado** una cita ajena, **cuando** USER intenta verla, **entonces** no obtiene acceso.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe API. |
| CA-02 | Pendiente | — | No existe API. |
