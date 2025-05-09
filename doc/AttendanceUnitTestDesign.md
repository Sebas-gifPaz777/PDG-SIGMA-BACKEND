# Diseño de Pruebas: AttendanceServiceImpl (Basado en AttendanceUnitTest)

Este documento describe los casos de prueba específicos implementados en la clase `AttendanceUnitTest` para verificar la funcionalidad de `AttendanceServiceImpl`.

**Objetivo:** Validar que los métodos probados en `AttendanceUnitTest` (`findByActivity`, `findByActivityAndStudent`, `save`, `delete`) funcionan correctamente según lo esperado, interactuando adecuadamente con los repositorios mockeados.

**Alcance:** Pruebas unitarias de un subconjunto de métodos de `AttendanceServiceImpl`, utilizando Mocks para las dependencias.

**Estrategia:** Pruebas de caja blanca y negra enfocadas en los flujos cubiertos por los tests existentes. Se usan aserciones para validar los resultados y el flujo de ejecución (verificando llamadas a mocks).

## Casos de Prueba (Implementados en `AttendanceUnitTest`)

### 1. Búsqueda de Asistencia

| ID Caso | Nombre del Test (Método)       | Descripción                                                               | Precondiciones (Mocks)                                                       | Pasos                                                                    | Resultado Esperado                                                                                             | Estado |
| :------ | :----------------------------- | :------------------------------------------------------------------------ | :--------------------------------------------------------------------------- | :----------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------- | :----- |
| ATTU-001 | `testFindByActivity`           | Verifica la búsqueda de asistencias por ID de actividad (caso existente). | `attendanceRepository.findByActivityId(1)` devuelve una lista con 1 `Attendance`. | 1. Llamar a `attendanceService.findByActivity(1)`.                       | La lista devuelta no está vacía, tiene tamaño 1 y contiene el objeto `Attendance` esperado (`assertFalse`, `assertEquals`). | OK     |
| ATTU-002 | `testFindByActivityAndStudent` | Verifica la búsqueda por ID de actividad y código de estudiante (existente). | `attendanceRepository.findByActivityIdAndStudentCode(1, "12345")` devuelve `Optional.of(attendance)`. | 1. Llamar a `attendanceService.findByActivityAndStudent(1, "12345")`. | El `Optional` devuelto está presente y contiene el objeto `Attendance` esperado (`assertTrue`, `assertEquals`).     | OK     |

---

### 2. Creación de Asistencia (`save`)

| ID Caso | Nombre del Test (Método) | Descripción                                                     | Precondiciones (Mocks)                                                                                                  | Pasos                                                           | Resultado Esperado                                                                                                                                                              | Estado |
| :------ | :----------------------- | :-------------------------------------------------------------- | :---------------------------------------------------------------------------------------------------------------------- | :-------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | :----- |
| ATTU-003 | `testSaveAttendance`     | Verifica la creación/guardado exitoso de un registro de asistencia. | `activityRepository.findById` devuelve `Optional.of(activity)`. `studentRepository.findById` devuelve `Optional.of(student)`. `attendanceRepository.save` devuelve el `Attendance` guardado. | 1. Llamar a `attendanceService.save(attendance)`.               | El `Attendance` devuelto no es nulo y tiene el ID esperado (`assertNotNull`, `assertEquals`). Se verifica que `attendanceRepository.save` fue llamado. (Cubre el caso ATT-001). | OK     |
|         |                          |                                                                 |                                                                                                                         |                                                                 | *Nota:* Este test no prueba explícitamente los casos donde Activity o Student no se encuentran, los cuales están cubiertos en `AttendanceServiceImplTest` (ATT-002, ATT-003). |        |

---

### 3. Eliminación de Asistencia (`delete`)

| ID Caso | Nombre del Test (Método) | Descripción                                                    | Precondiciones (Mocks)                                      | Pasos                                                           | Resultado Esperado                                                                                                    | Estado |
| :------ | :----------------------- | :------------------------------------------------------------- | :---------------------------------------------------------- | :-------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------- | :----- |
| ATTU-004 | `testDeleteAttendance`   | Verifica que se llama al método delete del repositorio sin errores. | `doNothing().when(attendanceRepository).delete(any(Attendance.class))` | 1. Llamar a `attendanceService.delete(attendance)`.           | La llamada al método no lanza excepciones (`assertDoesNotThrow`). Se verifica que `attendanceRepository.delete` fue llamado una vez. | OK     |
|         |                          |                                                                |                                                             |                                                                 | *Nota:* Este test no cubre el caso de intentar eliminar `null` (ATT-019).                                             |        |

---

**Casos de Prueba No Cubiertos en `AttendanceUnitTest` (Pero sí en `AttendanceServiceImplTest` o marcados como Pendientes):**

*   `testSave_ActivityNotFound` (ATT-002)
*   `testSave_StudentNotFound` (ATT-003)
*   `testFindByActivity_Empty` (ATT-007)
*   `testFindByActivityAndStudent_NotFound` (ATT-009)
*   `testFindAll` y `testFindAll_Empty` (ATT-010, ATT-011)
*   `testFindById_Found` y `testFindById_NotFound` (ATT-012, ATT-013)
*   `testUpdate` (ATT-014)
*   `testDeleteById` (ATT-018)
*   `testCount` (ATT-021)
*   Casos de prueba con valores `null` como entrada (ATT-004, ATT-005, ATT-015, ATT-019).

**Conclusión:**

La clase `AttendanceUnitTest` proporciona una cobertura básica para los métodos `findByActivity`, `findByActivityAndStudent`, `save` (caso exitoso) y `delete` de `AttendanceServiceImpl`. Para una cobertura completa, se deberían considerar los casos de prueba adicionales identificados en el diseño general (documento anterior) y/o añadir los tests faltantes a esta clase o a `AttendanceServiceImplTest`.