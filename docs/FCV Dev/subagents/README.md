# Catálogo operativo de subagentes

Los perfiles se basan en las plantillas verificadas en `prompts/subagents/`.
Todo encargo debe incluir objetivo, HU/CA/DoD, repo/rama, archivos permitidos,
referencia de contrato o diseño, autoridad de edición, prueba/evidencia y
fuera de alcance. Los verificadores son independientes y solo lectura.

| Perfil | Ámbito | Autoridad |
|---|---|---|
| `backend/01-domain-hexagonal` | invariantes, casos de uso y puertos | análisis o edición autorizada |
| `backend/02-persistence-mysql` | JPA, Flyway, 3FN, transacciones | análisis o edición autorizada |
| `backend/03-security-jwt` | auth, JWT, roles y ownership | análisis o edición autorizada |
| `backend/04-backend-verifier` | CA/DoD y pruebas backend | solo lectura |
| `frontend/01-design-reconciler` | fidelidad de diseño aprobado | solo lectura hasta aprobación |
| `frontend/02-api-integration` | REST, DTOs y sesión | edición autorizada |
| `frontend/03-state-forms` | formularios, estados y accesibilidad | edición autorizada |
| `frontend/04-frontend-verifier` | CA/DoD frontend | solo lectura |

No existe un ejecutor de subagentes disponible en la sesión actual. Las
asignaciones se registrarán aquí y las comprobaciones de verifier se harán en
una fase separada de solo lectura.
