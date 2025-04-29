# Diseño de Pruebas: DepartmentHeadController - Obtener Perfil (HeadDepartmentTest)

Este documento detalla los casos de prueba implementados en la clase `HeadDepartmentTest` para verificar la funcionalidad del endpoint `/department-head/profile/{id}` en `DepartmentHeadController`, responsable de obtener la información del perfil de un Jefe de Departamento.

**Objetivo:** Validar que el controlador maneja correctamente las solicitudes para obtener el perfil de un Jefe de Departamento, interactúa adecuadamente con `DepartmentHeadService`, y gestiona los casos de éxito y error (ej. jefe no encontrado).

**Alcance:** Pruebas unitarias/de integración ligera del endpoint `/department-head/profile/{id}` utilizando `@WebMvcTest`. Se mockean las dependencias (`DepartmentHeadService`, `HeadProgramRepository`) para aislar la prueba al controlador. *Nota: Mockear `HeadProgramRepository` aquí podría no ser estrictamente necesario si `DepartmentHeadService` ya está mockeado, pero no causa problemas.*

**Estrategia:** Simular peticiones HTTP GET utilizando `MockMvc`. Configurar el mock de `DepartmentHeadService` para devolver un `DepartmentHeadDTO` o lanzar una excepción según el escenario. Verificar los códigos de estado HTTP y el contenido de las respuestas JSON.

## Casos de Prueba (Implementados en `HeadDepartmentTest`)

### 1. Obtención de Perfil del Jefe de Departamento

| ID Caso  | Nombre del Test (Método)                    | Descripción                                                                              | Precondiciones (Mocks)                                                               | Pasos                                                                                              | Resultado Esperado                                                                                                                                  | Estado |
| :------- | :------------------------------------------ | :--------------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------- | :----- |
| HD-GP-001 | `testGetDepartmentHeadProfile_Success`      | Verifica la obtención exitosa de la información del perfil para un ID de jefe válido.   | `departmentHeadService.getProfile("12345")` devuelve un `DepartmentHeadDTO` válido. | 1. Definir `departmentHeadId`. 2. Configurar Mock del servicio. 3. Realizar petición GET a `/department-head/profile/{id}`. | Status HTTP 200 (OK). Cuerpo JSON contiene los datos esperados del DTO (school, program, rol, name) verificados con `jsonPath`.                | OK     |
| HD-GP-002 | `testGetDepartmentHeadProfile_NotFound`     | Verifica el manejo cuando se solicita el perfil de un ID de jefe de departamento que no existe. | `departmentHeadService.getProfile("99999")` lanza `Exception("No existe jefe...")`.    | 1. Definir `departmentHeadId` inexistente. 2. Configurar Mock del servicio para lanzar excepción. 3. Realizar petición GET. | Status HTTP 404 (Not Found). Cuerpo de la respuesta contiene el mensaje de error exacto devuelto por el servicio (`content().string(...)`). | OK     |
| HD-GP-003 | N/A                                         | Verifica el manejo de un error interno inesperado en el servicio.                         | `departmentHeadService.getProfile(id)` lanza `RuntimeException("Error interno")`.      | 1. Definir `departmentHeadId`. 2. Configurar Mock para lanzar `RuntimeException`. 3. Realizar petición GET.             | Status HTTP 500 (Internal Server Error). Cuerpo de la respuesta contiene un mensaje de error genérico (según implementación del controlador). | Pendiente |
| HD-GP-004 | N/A                                         | Verifica el comportamiento con un ID de formato inválido (si aplica validación).        | -                                                                                    | 1. Definir `departmentHeadId` con formato inválido. 2. Realizar petición GET.                                     | Status HTTP 400 (Bad Request) si hay validación de formato en el controlador o Spring.                                                            | Pendiente |

---

**Notas:**

*   Se utiliza `@WebMvcTest(DepartmentHeadController.class)` para enfocar el test en el controlador específico.
*   Se mockea la interfaz `DepartmentHeadService` para controlar el comportamiento de la lógica de negocio.
*   Se verifica el código de estado HTTP (`status().isOk()`, `status().isNotFound()`, etc.).
*   Se utiliza `jsonPath` para validar la estructura y los valores del cuerpo JSON en el caso exitoso.
*   Se utiliza `content().string()` para validar el mensaje de error exacto en el caso "Not Found".

Este diseño cubre los escenarios básicos (éxito y no encontrado) para el endpoint de obtención de perfil del Jefe de Departamento, tal como están implementados en `HeadDepartmentTest`.