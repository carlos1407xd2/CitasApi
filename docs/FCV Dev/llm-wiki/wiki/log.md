# Log de la LLM Wiki

- 2026-09-25 - LEARN - Se implementaron mis citas, historial, cancelacion y solicitudes de reprogramacion con decision ADMIN transaccional. - Resultado: 6 pruebas Maven verdes.

- 2026-09-25 - LEARN - Se implemento la agenda propia del profesional con bloques solapamiento-validado y slots atomicos de 30 minutos. - Resultado: API compilada y 6 pruebas Maven verdes.

- 2026-09-24 — INGEST — Se indexaron PRD, restricciones técnicas, requisitos
  3FN y el alcance S3–S5 aportado por el usuario. — Resultado: Wiki inicial.
- 2026-09-24 — LEARN — Se registraron el estado real de ambos repositorios y
  dos bloqueadores verificables. — Resultado: `estado-inicial` y DEC-001.
- 2026-09-24 — LINT — Enlaces internos iniciales revisados; no se añadió PII
  ni secretos. — Resultado: PASS estático.
- 2026-09-24 — LEARN — Bootstrap S2 verificado con Maven y MySQL existente.
  — Resultado: pruebas verdes y superficies health/OpenAPI/Swagger accesibles.
- 2026-09-24 — LINT — Se revisó el cambio de bootstrap y la separación de
  dominio/configuración. — Resultado: sin secretos versionados; DEC-001 sigue
  abierta.
- 2026-09-24 — DECISIÓN — El usuario autorizó generar `membership_number`
  sintético de laboratorio en backend para afiliación inicial opcional.
- 2026-09-24 — LEARN — Registro USER y afiliación opcional implementados y
  verificados contra MySQL con datos sintéticos. — Resultado: 201 con/sin
  plan, 400 por plan inválido, 409 por duplicado y cuatro pruebas verdes.
- 2026-09-24 — LEARN — Se añadió el catálogo público de planes activos como
  extensión compatible del contrato de registro. — Resultado: el cliente
  consume `{id, name}` sin duplicar nombres de catálogo en `users`.
- 2026-09-24 — LINT — Contrato cruzado validado. — Resultado: cinco pruebas
  Maven, tres pruebas Vitest, typecheck y build pasaron; `GET` local respondió
  `200` con exclusivamente planes activos.
- 2026-09-25 — LEARN — Se publicaron sedes y especialidades activas como
  catálogos REST de solo lectura, sin migración nueva. — Resultado: son la
  base contractual para disponibilidad y reservas del incremento S4.
- 2026-09-25 — LEARN — Se añadió consulta REST de slots libres por fecha y
  filtros de oferta. — Resultado: lectura sin efectos laterales; la retención
  transaccional queda explícitamente separada para el siguiente incremento.
- 2026-09-25 — DECISIÓN — La reserva obtiene `patientUserId` únicamente del
  sujeto autenticado y nunca del payload. — Resultado: el contrato conserva
  ownership; la activación requiere completar el proveedor JWT.
- 2026-09-25 — LEARN — Se añadió login JWT access y validación Bearer con roles
  desde `user_roles`; la API arrancó contra MySQL y health respondió `200`.
  — Resultado: la reserva ya tiene la frontera de ownership definida.
- 2026-09-29 — DECISIÓN — Se documentó el modelo relacional propio hasta 3FN,
  incluyendo dependencias funcionales, slots, reprogramación y auditoría.
- 2026-09-29 — HECHO — Se consolidó el contrato REST S4–S6 y se verificó el
  recorrido Docker de autenticación, perfil, catálogos y disponibilidad.
- 2026-09-29 — HECHO — Se comparó el modelo propio 3FN con la referencia del trainer y se documentaron diferencias compatibles, incluida la outbox V2.
