# Comparación del modelo 3FN con la referencia

La comparación se realizó contra `database/reference/db.sql`, sin copiar datos ni credenciales.

| Capacidad | Modelo propio / migraciones | Referencia | Resultado |
|---|---|---|---|
| Usuarios y roles | `users`, `roles`, `user_roles` | mismas relaciones | Compatible |
| EPS, régimen y plan | `eps`, `insurance_regimes`, `eps_plans` | catálogos separados | Compatible |
| Afiliación | `user_insurance_affiliations` | tabla puente con vigencia | Compatible |
| Profesionales | `professionals`, `professional_specialties`, `professional_locations` | relaciones N:M | Compatible |
| Agenda | `availability_blocks`, `professional_slots` | bloques y slots de 30 minutos | Compatible |
| Citas | `appointments` y estados por FK | cita con estado catalogado | Compatible |
| Historial | `appointment_status_history` | historial append-only | Compatible |
| Reprogramación | `reschedule_requests` y slots provisionales | solicitud separada | Compatible |
| Sesiones | `refresh_tokens`, password reset | tokens persistidos por usuario | Compatible |
| Eventos | `appointment_status_events` en V2 | extensión no destructiva | Adición compatible |

Las diferencias son intencionales: V2 agrega una outbox para publicación post-commit y la aplicación no duplica nombres de catálogos en tablas transaccionales. No se detectaron listas en columnas ni dependencias transitivas en los atributos agregados.
