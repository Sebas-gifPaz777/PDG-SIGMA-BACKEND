# Diseño de Pruebas: MonitorController - Obtener Perfil (MonitorProfileTest)

Este documento detalla los casos de prueba implementados en la clase `MonitorProfileTest` para verificar la funcionalidad del endpoint `/monitor/profile/{id}` en `MonitorController`, responsable de obtener la información del perfil de un Monitor.

**Objetivo:** Validar que el controlador maneja correctamente las solicitudes para obtener el perfil de un Monitor, interactúa adecuadamente con `MonitorServiceImpl`, y gestiona los casos de éxito y error (ej. monitor no encontrado).

**Alcance:** Pruebas unitarias/de integración ligera del endpoint `/monitor/profile/{id}` utilizando `@WebMvcTest`. Se mockea la dependencia `MonitorServiceImpl` para aislar la prueba al controlador.

**Estrategia:** Simular peticiones HTTP GET utilizando `MockMvc`. Configurar el mock de `MonitorServiceImpl` para devolver un `MonitorDTO` o lanzar una excepción según el escenario. Verificar los códigos de estado HTTP y el contenido de las respuestas JSON.

## Casos de Prueba (Implementados en `MonitorProfileTest`)

### 1. Obtención de Perfil del Monitor

| ID Caso | Nombre del Test (Método)        | Descripción                                                                      | Precondiciones (Mocks)                                                           | Pasos                                                                                    | Resultado Esperado                                                                                                                             | Estado |
| :------ | :------------------------------ | :------------------------------------------------------------------------------- | :------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------- | :----- |
| MON-GP-001 | `testProfileMonitorFound`       | Verifica la obtención exitosa de la información del perfil para un ID de monitor válido. | `monitorService.getProfile("123")` devuelve un `MonitorDTO` válido.            | 1. Definir `monitorId`. 2. Configurar Mock del servicio. 3. Realizar petición GET a `/monitor/profile/{id}`. | Status HTTP 200 (OK). Cuerpo JSON contiene los datos esperados del DTO (school, program, rol, name) verificados con `jsonPath`.           | OK     |
| MON-GP-002 | `testProfileMonitorNotFound`    | Verifica el manejo cuando se solicita el perfil de un ID de monitor que no existe.     | `monitorService.getProfile("999")` lanza `Exception("No existe monitor...")`. | 1. Definir `monitorId` inexistente. 2. Configurar Mock para lanzar excepción. 3. Realizar petición GET. | Status HTTP 404 (Not Found). Cuerpo de la respuesta contiene el mensaje de error exacto devuelto por el servicio (`content().string(...)`). | OK     |
| MON-GP-003 | N/A                             | Verifica el manejo de un error interno inesperado en el servicio.                  | `monitorService.getProfile(id)` lanza `RuntimeException("Error interno")`.       | 1. Definir `monitorId`. 2. Configurar Mock para lanzar `RuntimeException`. 3. Realizar petición GET.          | Status HTTP 500 (Internal Server Error). Cuerpo de la respuesta contiene un mensaje de error genérico (según el controlador).                  | Pendiente |
| MON-GP-004 | N/A                             | Verifica el comportamiento con un ID de formato inválido (si aplica validación). | -                                                                                | 1. Definir `monitorId` con formato inválido. 2. Realizar petición GET.                   | Status HTTP 400 (Bad Request) si hay validación de formato en el controlador o Spring.                                                     | Pendiente |

---

**Notas:**

*   Se utiliza `@WebMvcTest(MonitorController.class)` para enfocar el test en el controlador específico.
*   Se mockea `MonitorServiceImpl` para controlar el comportamiento de la lógica de negocio.
*   Se verifica el código de estado HTTP (`status().isOk()`, `status().isNotFound()`, etc.).
*   Se utiliza `jsonPath` para validar la estructura y los valores del cuerpo JSON en el caso exitoso.
*   Se utiliza `content().string()` para validar el mensaje de error exacto en el caso "Not Found".
*   Las rutas en `mockMvc.perform` son relativas (`/monitor/profile/{id}`).

Este diseño cubre los escenarios básicos (éxito y no encontrado) para el endpoint de obtención de perfil del Monitor, tal como están implementados en `MonitorProfileTest`.