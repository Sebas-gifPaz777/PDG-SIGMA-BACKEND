INSERT INTO public.activity (id, name, creation_date, finish_date, role_creator, role_responsable, category, description, monitoring_id, professor_id, monitor_id, state, delivery_date, semester, edited_date) VALUES
(29, 'MP Calificar quiz 27', '2025-02-24', '2025-02-26', 'r', 'M', 'Académico', 'Camilaa asignado', 4, 1234567, 'A00379506', 'PENDIENTE', NULL, '2025-1', NULL),
(38, 'Test', '2025-02-24', '2025-02-26', 'P', 'M', 'Extracurricular', 'ddfdf', 4, 1234567, 'A00379506', 'COMPLETADO', '2025-02-24', '2025-1', '2025-02-24'),
(39, 'Explicar primer tema', '2025-02-24', '2025-02-25', 'P', 'M', 'Académico', 'Explicar', 6, 1234567, 'A00379506', 'COMPLETADO', '2025-02-24', '2025-1', '2025-02-24'),
(36, 'IS1 - Actualizar registros en la db', '2025-02-24', '2025-02-25', 'P', 'M', 'Académico', 'Agregar estudiantes a la base de datos local', 3, 1234567, 'A00379506', 'COMPLETADO', '2025-02-24', '2025-1', '2025-02-24'),
(32, 'Calificar seguimientos', '2025-02-24', '2025-02-26', 'P', 'M', 'Académico', 'Calificar minicuisamente', 3, 1234567, 'A00379506', 'COMPLETADO', '2025-02-24', '2025-1', '2025-02-24'),
(41, 'Calificacion', '2025-02-24', '2025-02-26', 'P', 'M', 'Académico', 'Calificar examen', 3, 1234567, 'A00369501', 'COMPLETADO', '2025-02-24', '2025-1', '2025-02-24'),
(31, 'Calificacion Y Revisión', '2025-02-24', '2025-03-12', 'P', 'M', 'Académico', '28 feb - 11 marz - cami', 3, 1234567, 'A00379506', 'COMPLETADO', '2025-02-24', '2025-1', '2025-02-24'),
(33, 'Revisión Quices', '2025-02-24', '2025-02-25', 'P', 'M', 'Académico', 'Calificar quices unidad 1', 3, 1234567, 'A00369501', 'COMPLETADO', '2025-02-24', '2025-1', '2025-02-24'),
(43, 'Quices unidad 4', '2025-02-25', '2025-02-27', 'P', 'M', 'Extracurricular', 'Calificar quices unidad 4', 3, 1234567, 'A00369501', 'COMPLETADO', '2025-02-25', '2025-1', '2025-02-25');

INSERT INTO public.course (id, name, program_id) VALUES
(1, 'Fisiología', 1),
(2, 'Histología', 1),
(3, 'Análisis de laboratorio en el proceso Salud Enfermedad', 2),
(4, 'Dinámica celular', 2),
(5, 'Formación Cuantitativa I', 3),
(6, 'Innovación Tech', 3),
(7, 'Macroeconomía y Productividad I', 4),
(8, 'Global Business I', 4),
(9, 'Algoritmos y Programación I', 5),
(10, 'Ingeniería de Software I', 5),
(11, 'Fundamentos de Diseño I', 6),
(12, 'Fundamentos de Diseño II', 6),
(13, 'Mundos Posibles I', 7),
(14, 'Identidades y alteridades II', 7),
(15, 'Analisis Político I', 8),
(16, 'Analisis Político II', 8);

INSERT INTO public.monitor (code, name, last_name, semester, grade_average, grade_course, email) VALUES
('A00379504', 'Sofia', 'Martinez', 3, 4.6, 4.8, 'pazsebas797@gmail.com'),
('A00379506', 'Camila', 'Rodriguez', 2, 4.8, 4.5, 'pazsebas797@gmail.com'),
('A00369501', 'Luis', 'Fernandez', 5, 4.5, 4.5, 'pazsebas797@gmail.com');

INSERT INTO public.monitoring (id, start_date, finish_date, average_grade, course_grade, semester, course_id, professor_id, school_id, program_id) VALUES
(3, '2025-02-19', '2025-03-12', 4.5, 4.5, '2024-2', 10, 1234567, 3, 5),
(4, '2025-02-21', '2025-02-24', 4.5, 4.5, '2024-2', 13, 1234567, 4, 7),
(6, '2025-02-23', '2025-02-26', 4.5, 4.5, '2024-2', 6, 1234567, 2, 3);

INSERT INTO public.monitoring_monitor (id, monitoring_id, monitor_id) VALUES
(1, 3, 'A00379506'),
(2, 4, 'A00379506'),
(3, 3, 'A00369501'),
(4, 6, 'A00379506');

INSERT INTO public.professor (id, name, password) VALUES
(1234567, 'Juan Caldas', 'password'),
(89123456, 'Laura Diaz', 'password'),
(78912345, 'Luis Guerra', 'password'),
(67890123, 'Pablo Suarez', 'password');

INSERT INTO public.program (id, name, school_id) VALUES
(1, 'Medicina', 1),
(2, 'Bacteriología y Laboratorio Clínico', 1),
(3, 'Administración de Empresas con Enfásis en Negocios Internacionales', 2),
(4, 'Economía y Negocios Internacionales', 2),
(5, 'Ingeniería de Sistemas', 3),
(6, 'Diseño de Medios Interactivos', 3),
(7, 'Antropología', 4),
(8, 'Ciencia Política', 4);
