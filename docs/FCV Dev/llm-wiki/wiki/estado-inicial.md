# Estado inicial verificado

## HECHOS

- `citas-api` está en `develop` y ahora contiene un bootstrap Maven/Spring Boot
  con dominio inicial, configuración MySQL/Flyway, CORS explícito, Actuator y
  OpenAPI. La base existente permanece en versión Flyway 2; no se modificó V1.
- `citas-web` está en `develop`, no contiene proyecto importado; conserva
  `portal-de-citas.zip`, artefacto AI Studio con `mockData.ts` y dependencias
  Express incompatibles con el alcance.
- Docker CLI está instalado, pero el engine no acepta la conexión del entorno.
- Docker fue validado mediante la infraestructura del workspace: MySQL ya tenía
  19 tablas y no se cargó ni recreó el esquema. La máquina tiene Node 24; Maven
  se ejecuta dentro del contenedor Java 21.

## PREGUNTAS ABIERTAS

- Falta evidencia explícita de aprobación del diseño Stitch.
- Falta una decisión de contrato para la afiliación inicial opcional.

## EVIDENCIA

- `mvn test` verde: 1 prueba de política de zona de negocio.
- Rutas verificadas contra MySQL: `/actuator/health` y `/v3/api-docs` con 200;
  `/swagger-ui/index.html` con redirección 302.
- [[../../wiki/scrum/README|Índice Scrum]].
