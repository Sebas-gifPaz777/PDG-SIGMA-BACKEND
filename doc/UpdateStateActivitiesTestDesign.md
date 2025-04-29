# Diseño de Pruebas: ActivityController - Actualizar Estado (UpdateStateActivitiesTest)

Este documento describe los casos de prueba implementados (actualmente comentados) en la clase `UpdateStateActivitiesTest` para verificar la funcionalidad del endpoint `/activity/updateState` en `ActivityController`, responsable de actualizar el estado de una actividad.

**Objetivo:** Validar que el controlador maneja correctamente las solicitudes PUT para actualizar el estado de una actividad, interactúa adecuadamente con `ActivityServiceImpl`, y gestiona los casos de éxito y error (ej. actividad no encontrada).

**Alcance:** Pruebas unitarias/de integración ligera del endpoint `/activity/updateState` (PUT) utilizando `@WebMvcTest`. Se mockea la dependencia `ActivityServiceImpl` para aislar la prueba al controlador.

**Estrategia:** Simular peticiones HTTP PUT con el ID de la actividad en el cuerpo (como texto plano, según el código) utilizando `MockMvc`. Configurar el mock de `ActivityServiceImpl` para que el método `updateState` se ejecute sin errores o lance una excepción según el escenario. Verificar los códigos de estado HTTP y el contenido de las respuestas (texto plano).

## Casos de Prueba (Implementados como Comentados en `UpdateStateActivitiesTest`)

### 1. Actualización de Estado de Actividad (`/activity/updateState`)

| ID Caso  | Nombre del Test (Método)           | Descripción                                                                                           | Precondiciones (Mocks)                                                                | Pasos                                                                                                   | Resultado Esperado                                                                                                                                       | Estado    |
| :------- | :--------------------------------- | :---------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------ | :------------------------------------------------------------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------- | :-------- |
| ACT-US-001 | `testSetActivityState_Success`     | Verifica la actualización exitosa del estado de una actividad existente.                            | `activityService.updateState("1")` devuelve `true` (o se ejecuta sin error si es `void`). | 1. Definir `activityId`. 2. Configurar Mock del servicio. 3. Realizar petición PUT a `/activity/updateState` con ID en el cuerpo. | Status HTTP 200 (OK). Cuerpo de la respuesta (texto plano) es "Estado cambiado". Se verifica la llamada a `activityService.updateState`.                   | Comentado |
| ACT-US-002 | `testSetActivityState_NotFound`    | Verifica el manejo cuando se intenta actualizar el estado de una actividad que no existe.           | `activityService.updateState("999")` lanza `Exception("No se encontró...")`.        | 1. Definir `activityId` inexistente. 2. Configurar Mock para lanzar excepción. 3. Realizar petición PUT. | Status HTTP 500 (Internal Server Error) (según el controlador actual). Cuerpo de la respuesta debería contener el mensaje de error (ver Nota).         | Comentado |
| ACT-US-003 | N/A                                | Verifica el manejo de un error interno inesperado durante la actualización del estado en el servicio. | `activityService.updateState(id)` lanza `RuntimeException("Error DB")`.               | 1. Definir `activityId`. 2. Configurar Mock para lanzar `RuntimeException`. 3. Realizar petición PUT.       | Status HTTP 500 (Internal Server Error). Cuerpo de la respuesta contiene el mensaje de la excepción (`e.getMessage()`) según el controlador actual. | Pendiente |
| ACT-US-004 | N/A                                | Verifica el comportamiento si el cuerpo de la solicitud está vacío o no contiene un ID válido.    | -                                                                                     | 1. Realizar petición PUT sin cuerpo o con cuerpo inválido.                                          | Status HTTP 400 (Bad Request) o 500 (si el servicio lanza NPE u otra excepción al recibir un ID nulo/inválido).                                        | Pendiente |

---

**Notas:**

*   **Tests Comentados:** Los tests principales están actualmente comentados en el archivo `UpdateStateActivitiesTest`. Necesitan ser descomentados para ser ejecutados.
*   **Rutas Relativas:** El código utiliza correctamente rutas relativas (`/activity/updateState`) en `mockMvc.perform`, lo cual es la práctica recomendada.
*   **Mocking de `updateState`**: Es crucial verificar si el método `activityService.updateState` devuelve `boolean` o es `void`.
    *   Si devuelve `boolean`: `when(...).thenReturn(true/false)` y `when(...).thenThrow(...)` es correcto.
    *   Si es `void`: Usar `doNothing().when(...)` para el caso exitoso y `doThrow(...).when(...)` para el caso de error.
*   **Cuerpo de la Solicitud PUT**: El test envía el `activityId` como texto plano en el cuerpo (`.content(activityId)`). Esto asume que el método del controlador (`setActivityState`) está anotado con `@RequestBody String id`.
*   **Manejo de Error en `testSetActivityState_NotFound`**: El test comentado espera un `status().isInternalServerError()`. Si la intención es devolver un `404 Not Found` cuando la actividad no se encuentra, la lógica `catch` en `ActivityController` debería modificarse para detectar esa excepción específica y devolver `ResponseEntity.status(HttpStatus.NOT_FOUND).body(...)`. La aserción del test también debería cambiarse a `status().isNotFound()`. La verificación del cuerpo del error (`.andExpect(content().string(...))`) está comentada en el test; debería descomentarse y ajustarse al mensaje esperado.

Este diseño cubre los escenarios básicos de éxito y "no encontrado" para la actualización de estado, tal como están (comentados) en `UpdateStateActivitiesTest`. Se recomienda descomentar los tests, ajustar el mocking según el tipo de retorno de `updateState`, y refinar el manejo de errores/códigos de estado en el controlador y los tests si es necesario.