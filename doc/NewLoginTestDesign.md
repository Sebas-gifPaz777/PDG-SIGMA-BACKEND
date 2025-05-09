# Diseño de Pruebas: AuthController - Endpoint de Login (NewLoginTest)

Este documento detalla los casos de prueba implementados en la clase `NewLoginTest` para verificar la funcionalidad del endpoint de autenticación (`/auth/login`) en `AuthController`.

**Objetivo:** Validar que el controlador maneja correctamente las solicitudes de login, interactúa adecuadamente con `AuthService`, y gestiona los casos de éxito (devolviendo el rol) y error (credenciales inválidas o usuario no encontrado).

**Alcance:** Pruebas unitarias/de integración ligera del endpoint `/auth/login` (POST) utilizando `@WebMvcTest`. Se mockea la dependencia `AuthService` para aislar la prueba al controlador.

**Estrategia:** Simular peticiones HTTP POST con cuerpo JSON utilizando `MockMvc`. Configurar el mock de `AuthService` para devolver un `AuthDTO` (con el rol) en caso de éxito o lanzar una excepción en caso de fallo. Verificar los códigos de estado HTTP y el contenido de las respuestas (JSON o texto plano).

## Casos de Prueba (Implementados en `NewLoginTest`)

### 1. Autenticación de Usuario (`/auth/login`)

| ID Caso  | Nombre del Test (Método) | Descripción                                                                       | Precondiciones (Mocks)                                                              | Pasos                                                                                      | Resultado Esperado                                                                                                                             | Estado |
| :------- | :----------------------- | :-------------------------------------------------------------------------------- | :---------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------- | :----- |
| AUTH-L-001 | `loginUser_Success`      | Verifica el login exitoso cuando el servicio de autenticación devuelve un rol válido. | `authService.loginUser(any(AuthDTO.class))` devuelve `new AuthDTO("student")`.      | 1. Construir cuerpo JSON con credenciales (simuladas). 2. Configurar Mock. 3. Realizar POST a `/auth/login`. | Status HTTP 200 (OK). Cuerpo JSON contiene `{"role": "student"}`.                                                                               | OK     |
| AUTH-L-002 | `loginUser_Failure`      | Verifica el manejo de un login fallido cuando el servicio lanza una excepción.      | `authService.loginUser(any(AuthDTO.class))` lanza `Exception("No hay un usuario...")`. | 1. Construir cuerpo JSON con credenciales (simuladas). 2. Configurar Mock. 3. Realizar POST a `/auth/login`. | Status HTTP 400 (Bad Request). Cuerpo de la respuesta (texto plano) contiene el mensaje de error "No hay un usuario con este id o contraseña". | OK     |
| AUTH-L-003 | N/A                      | Verifica el manejo de un cuerpo de solicitud JSON mal formado.                    | - (Manejo por Spring/Jackson)                                                       | 1. Construir cuerpo JSON inválido. 2. Realizar POST a `/auth/login`.                          | Status HTTP 400 (Bad Request).                                                                                                                | Pendiente |
| AUTH-L-004 | N/A                      | Verifica el manejo si falta el campo `userId` en el cuerpo JSON.                  | - (Depende de validaciones en `AuthDTO` o el controlador)                           | 1. Construir cuerpo JSON sin `userId`. 2. Realizar POST a `/auth/login`.                      | Status HTTP 400 (Bad Request) si hay validación.                                                                                              | Pendiente |
| AUTH-L-005 | N/A                      | Verifica el manejo si falta el campo `password` en el cuerpo JSON.                | - (Depende de validaciones en `AuthDTO` o el controlador)                           | 1. Construir cuerpo JSON sin `password`. 2. Realizar POST a `/auth/login`.                    | Status HTTP 400 (Bad Request) si hay validación.                                                                                              | Pendiente |
| AUTH-L-006 | N/A                      | Verifica el manejo de un error interno inesperado en el servicio.                 | `authService.loginUser` lanza `RuntimeException("Error DB")`.                       | 1. Construir cuerpo JSON. 2. Configurar Mock. 3. Realizar POST a `/auth/login`.                | Status HTTP 500 (Internal Server Error). Cuerpo de la respuesta contiene un mensaje genérico de error.                                        | Pendiente |

---

**Notas:**

*   Se utiliza `@WebMvcTest(AuthController.class)` para enfocar el test en el controlador específico.
*   Se mockea `AuthService` para simular la lógica de autenticación y controlar las respuestas.
*   Se verifica el código de estado HTTP (`status().isOk()`, `status().isBadRequest()`, etc.).
*   Se utiliza `jsonPath` para validar la estructura y los valores del cuerpo JSON en el caso exitoso.
*   Se utiliza `content().string()` para validar el mensaje de error exacto en el caso de fallo esperado.
*   Los casos "Pendientes" sugieren pruebas adicionales para robustecer la validación de entradas y el manejo de errores inesperados. La validación de campos faltantes o JSON mal formado a menudo es manejada automáticamente por Spring Boot, resultando en un 400 Bad Request.

Este diseño cubre los escenarios básicos de éxito y fracaso para el endpoint de login, tal como están implementados en `NewLoginTest`.