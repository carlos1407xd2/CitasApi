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
2. S3: catálogos, profesionales, disponibilidad, búsqueda, reservas y decisión.
3. S4: perfil, recuperación, EPS/planes, ciclo de vida, agenda e historial.
4. S5: OpenAPI y webhook post-commit con payload sin PII.

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
