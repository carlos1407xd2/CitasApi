# `citas-api` — instrucciones del agente backend

## Alcance

Este repositorio contiene dominio, aplicación, puertos, adaptadores de
persistencia/REST, seguridad, migraciones, pruebas y automatizaciones n8n del
laboratorio. No editar `../citas-web` desde aquí.

## Arquitectura

- Java 21, Spring Boot 3.5.x y Maven.
- El dominio no depende de Spring, JPA, JDBC ni HTTP.
- Los casos de uso viven en `application`; REST y persistencia son adaptadores.
- MySQL 8.4 es la persistencia objetivo; el esquema 3FN existente es canónico.
- No recrear ni modificar V1. Todo cambio usa Flyway posterior y compatible.

## Flujo de trabajo

1. Leer la HU, CA y DoD en `docs/wiki/scrum/`.
2. Para contratos REST, revisar `docs/FCV Dev/llm-wiki/wiki/contrato-rest-s2-s5.md`.
3. Proponer/actualizar el plan cross-repo antes de tocar contratos.
4. Implementar el mínimo coherente y probarlo.
5. Ejecutar una verificación independiente de solo lectura antes de cerrar.

## Seguridad

- Secretos exclusivamente por variables de entorno; no abrir ni registrar `.env`.
- Nunca registrar passwords, refresh tokens, JWT ni PII.
- Usar datos sintéticos del laboratorio.
- Aplicar roles y ownership en el backend.

## Bloqueos actuales

- No implementar registro con afiliación opcional hasta resolver
  `docs/FCV Dev/llm-wiki/wiki/DEC-001-afiliacion-inicial-opcional.md`.
