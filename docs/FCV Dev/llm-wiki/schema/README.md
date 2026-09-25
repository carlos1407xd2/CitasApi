# Convenciones de la LLM Wiki

La Wiki contiene conocimiento durable, no transcripciones.

- `raw/`: fuentes curadas e inmutables.
- `wiki/`: síntesis verificadas; cada afirmación debe clasificarse como
  **HECHO**, **DECISIÓN**, **PREFERENCIA** o **PREGUNTA ABIERTA**.
- `wiki/index.md`: punto de entrada y catálogo.
- `wiki/log.md`: registro append-only de ingest, consultas, cambios y lint.

Al modificar la estructura se actualiza `index.md`. El lint comprueba enlaces,
duplicados, afirmaciones sin fuente, contradicciones y datos sensibles.
