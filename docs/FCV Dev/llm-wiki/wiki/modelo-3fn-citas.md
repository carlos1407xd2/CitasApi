# Modelo relacional y normalización 3FN

## Decisión

El modelo usa catálogos por FK y tablas puente para relaciones N:M. Los nombres
de EPS, régimen, plan y especialidad no se duplican en usuarios, profesionales
ni citas. La duración pertenece a la especialidad y la reserva se representa
con slots asociados a la cita para impedir doble ocupación.

## Dependencias funcionales principales

- `users.id -> document_type, document_number, email, ...`; documento y email tienen restricciones únicas.
- `specialties.id -> code, name, duration, general, active`.
- `professionals.id -> user_id, professional_code, license_number, active`.
- `(professional_id, specialty_id) -> is_primary` en la tabla puente.
- `(professional_id, location_id) -> habilitación` en la tabla puente.
- `appointments.id -> patient_user_id, professional_id, specialty_id, location_id, start_at, end_at, status_id`.
- `(appointment_id, slot_start) -> slot reservation`; la restricción única impide doble ocupación.

## 1FN → 2FN → 3FN

En 1FN los atributos son atómicos y las listas de especialidades, sedes y slots están separadas. En 2FN las relaciones N:M usan tablas puente y sus atributos dependen de la clave completa. En 3FN los datos descriptivos viven en catálogos; las citas referencian IDs y los estados se almacenan mediante catálogos e historial, evitando dependencias transitivas.

## Reprogramación y auditoría

`reschedule_requests` conserva la solicitud provisional y su estado, de modo que la cita original no cambia hasta la aprobación. `appointment_status_history` es append-only y registra cita, estado, actor, fuente, fecha y motivo.

## Índices operativos

Se requieren índices por profesional/fecha, sede/fecha, estado/fecha y una restricción única sobre la combinación de cita y slot. Los índices de la outbox cubren eventos pendientes y consulta por cita.
