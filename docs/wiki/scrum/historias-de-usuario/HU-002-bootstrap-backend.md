---
id: HU-002
tipo: historia-de-usuario
titulo: "Bootstrap backend"
estado: En desarrollo
epica: "[[EP-001-fundacion-y-seguridad]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1"
dependencias:
  - "[[HU-001-modelo-3fn-y-migraciones]]"
---
# HU-002 — Bootstrap backend
**COMO** equipo del laboratorio  
**QUIERO** una API Spring Boot con arquitectura hexagonal y configuración segura  
**PARA** implementar funcionalidades sin acoplar el dominio a adaptadores.

## Criterios de aceptación
### CA-01 — Base ejecutable
**Dado** el repositorio, **cuando** se inicia con la configuración de laboratorio, **entonces** la API expone salud y se conecta al esquema configurado.
### CA-02 — Separación de capas
**Dado** un caso de uso, **cuando** se inspecciona el código, **entonces** no depende directamente de Spring, JPA ni JDBC en el dominio.

## Definition of Done
- [ ] Maven, Java 21, Spring Boot 3.5.x y Flyway configurados.
- [ ] Configuración sin secretos versionados.
- [ ] Pruebas base pasan.

## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | No existe proyecto Maven. |
| CA-02 | Pendiente | — | No existe código fuente. |
