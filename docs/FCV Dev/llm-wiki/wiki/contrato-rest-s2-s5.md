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

1. S2: registro, login, refresh y logout — **bloqueado parcialmente por DEC-001**.
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
