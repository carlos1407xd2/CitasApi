# Plan de contrato REST S2–S5

## DECISIÓN DE PROCESO

Todo cambio de contrato se implementa solo después de revisar este plan y de
actualizar la especificación Scrum correspondiente.

## Repositorios y archivos previstos

| Repositorio | Áreas | Compatibilidad | Verificación |
|---|---|---|---|
| `citas-api` | controladores REST, DTOs, casos de uso, seguridad, pruebas y OpenAPI | versionar bajo `/api/v1`; no cambiar contratos publicados sin compatibilidad | pruebas de aplicación, persistencia y MockMvc |
| `citas-web` | cliente REST, tipos, formularios, rutas y pruebas | consumir URL de entorno; no BFF/Express | Vitest, lint, build y recorrido Docker |

## Fases de contrato

1. S2: `POST /api/v1/auth/register` recibe nombres, apellidos, documento,
   email, teléfono, contraseña e `insurancePlanId` opcional; devuelve 201 sin
   password ni nombres de catálogos. Un plan inexistente/inactivo devuelve 400
   y duplicados devuelven 409. Login, refresh y logout se añadirán de forma
   compatible en esta familia de rutas.
   `GET /api/v1/catalogs/insurance-plans` es público y devuelve únicamente
   planes activos, ordenados por nombre, con `{id, name}`. Es un contrato
   aditivo para que el registro pueda enviar el identificador FK opcional;
   no devuelve ni acepta números de póliza.
   `GET /api/v1/catalogs/locations` y `GET /api/v1/catalogs/specialties`
   devuelven únicamente catálogos activos. Las especialidades incluyen su
   duración `30|60`, indicador de generalidad y si requieren aprobación ADMIN.
   `GET /api/v1/availability?date=YYYY-MM-DD` devuelve slots libres del día.
   Acepta filtros opcionales `locationId`, `specialtyId` y `professionalId`,
   y responde identificadores y fechas `startAt/endAt`; no reserva ni retiene
   slots. La reserva transaccional se implementará en el siguiente incremento.
   `POST /api/v1/appointments` requiere usuario autenticado y recibe
   `professionalId`, `locationId`, `specialtyId`, `startAt` y `reason` opcional.
   El usuario se obtiene del sujeto autenticado y no de `patientUserId` en el
   payload. General responde `APPROVED`; especializada responde `REQUESTED`;
   un slot ocupado responde `409 SLOT_NOT_AVAILABLE`.
   `POST /api/v1/auth/login` recibe `email` y `password` y devuelve un access
   JWT de corta duración con el sujeto de usuario y roles. El secreto se lee
   exclusivamente desde `JWT_ACCESS_SECRET`; refresh/logout aún requieren su
   siguiente incremento antes de declararse completos.
2. S3: catálogos, profesionales, disponibilidad, búsqueda, reservas y decisión.
3. S4: perfil, recuperación, EPS/planes, ciclo de vida, agenda e historial.
4. S5: OpenAPI y webhook post-commit con payload sin PII.

## Refresh y logout

`POST /api/v1/auth/refresh` recibe el refresh token y devuelve un access JWT
nuevo; `POST /api/v1/auth/logout` revoca ese refresh token. Solo se persiste
el hash del token en `refresh_tokens`.

Las operaciones ADMIN de profesionales son `POST /api/v1/admin/professionals`,
`PUT /api/v1/admin/professionals/{id}/specialties`,
`PUT /api/v1/admin/professionals/{id}/locations` y
`PATCH /api/v1/admin/professionals/{id}/active`; todas requieren rol ADMIN.
La contraseña temporal solo se recibe, se hashea y nunca se devuelve.

## Reglas compartidas confirmadas

- Base URL configurable por entorno y versión `/api/v1`.
- Fechas `YYYY-MM-DD`, horas `HH:mm` y zona de negocio `America/Bogota`.
- Errores documentados para `400`, `401`, `403`, `404` y `409`.
- El backend es autoridad de validación, roles, ownership, disponibilidad y
  transiciones.

## Migración y rollback conceptual

- La base 3FN existente es canónica: no se modifica ni recrea V1.
- Cambios de esquema usan migraciones Flyway posteriores, compatibles y con
  rollback documentado antes de ejecutarse.
- El cliente se adapta a respuestas aditivas antes de eliminar campos o rutas.

## Agenda del profesional

Las operaciones del profesional sobre su propia agenda son:

- `GET /api/v1/professional/availability-blocks` devuelve sus bloques ordenados por fecha y hora.
- `POST /api/v1/professional/availability-blocks` recibe `{locationId,date,startTime,endTime}`. La fecha no puede ser anterior al día actual, las horas deben caer en intervalos de 30 minutos y la ubicación debe estar asignada al profesional. Cada bloque crea slots atómicos de 30 minutos.
- `PATCH /api/v1/professional/availability-blocks/{id}` permite cambiar datos del bloque o desactivarlo, únicamente si pertenece al profesional autenticado.
- `DELETE /api/v1/professional/availability-blocks/{id}` elimina el bloque y sus slots libres. Los bloques con alguna cita no se pueden modificar ni eliminar y responden `409 BLOCK_HAS_APPOINTMENTS`.

Estas rutas requieren rol `PROFESSIONAL`; el usuario se obtiene del sujeto JWT y nunca se acepta `professionalId` en el payload. Los solapamientos responden `409 BLOCK_OVERLAP` y las ubicaciones no asignadas responden `404 LOCATION_NOT_ASSIGNED`.
