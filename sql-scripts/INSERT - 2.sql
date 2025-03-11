-- Inserts para la tabla school
INSERT INTO public.school (name) VALUES
('Barberi de Ingeniería y Diseño'),
('Escuela de Ciencias Sociales');

-- Inserts para la tabla program
INSERT INTO public.program (name, school_id) VALUES
('Ingeniería de Sistemas', 1),
('Diseño de Medios Interactivos', 1),
('Ingeniería Industrial', 1),
('Diseño Industrial', 1),
('Antropología', 2),
('Sociología', 2),
('Ciencias Políticas', 2),
('Psicología', 2);

-- Inserts para la tabla professor
INSERT INTO public.professor (id, name, password) VALUES
(1234567, 'Claudia Castiblanco', 'password'),
(12345678, 'Emilia Rocío Segovia', 'password');

-- Inserts para la tabla course
INSERT INTO public.course (name, program_id) VALUES
('Sistemas Intensivos en Datos 1', 1),
('Sistemas Intensivos en Datos 2', 1),
('Algoritmos y Programación 1', 1),
('Algoritmos y Programación 2', 1),
('Algoritmos y Programación 3', 1),
('Computación y Estructuras Discretas 1', 1),
('Computación y Estructuras Discretas 2', 1),
('Computación y Estructuras Discretas 3', 1),
('Gerencia de Proyectos', 1),
('Cibersegruidad', 1),
('Sistemas Operativos', 1),
('Proyecto Integrador', 1),
('Ingeniería de Software 1', 1),
('Ingeniería de Software 2', 1),
('Ingeniería de Software 3', 1),
('Ingeniería de Software 4', 1),
('Ingeniería de Software 5', 1),
('Neuropsicología', 2);

-- Inserts para la tabla student
INSERT INTO public.student (code, name) VALUES
('A00365886', 'Juan Diego Lora'),
('A00364725', 'Daniela Olarte'),
('A00364813', 'Sebastian Paz'),
('A00365545', 'Lauren Rojas'),
('A00365821', 'Vanessa Perdomo');

-- Inserts para la tabla student_course
INSERT INTO public.student_course (course_id, student_id) VALUES
(1, 'A00365886'),
(2, 'A00365886'),
(3, 'A00365886'),
(1, 'A00364725'),
(2, 'A00364725'),
(3, 'A00364725'),
(6, 'A00364725'),
(2, 'A00364813'),
(3, 'A00364813'),
(4, 'A00364813'),
(5, 'A00364813'),
(6, 'A00364813');

-- Inserts para la tabla monitor
INSERT INTO public.monitor (code, name, last_name, semester, grade_average, grade_course, email) VALUES
('A00365713', 'Yuluka', 'Gigante', 9, 4.8, 4.6, 'juandiegoloralara@gmail.com'),
('A00366522', 'Carlos', 'Bolaños', 9, 4.3, 4.5, 'juanlora1@protonmail.com'),
('A00400122', 'Alejandro', 'Londoño', 7, 4.6, 4.4, 'juan.lora1@u.icesi.edu.co');

-- Inserts para la tabla monitoring
INSERT INTO public.monitoring (start_date, finish_date, average_grade, course_grade, semester, course_id, professor_id, school_id, program_id) VALUES
('2025-02-01', '2025-06-30', 4.2, 4.5, '2025-1', 1, 1234567, 1, 1),
('2025-02-01', '2025-03-30', 4.0, 4.3, '2025-1', 2, 1234567, 1, 1),
('2025-02-01', '2025-03-30', 4.0, 4.3, '2025-1', 3, 12345678, 1, 1),
('2025-02-01', '2025-03-30', 4.5, 4.3, '2025-1', 4, 1234567, 1, 1),
('2025-02-01', '2025-03-30', 4.5, 4.3, '2025-1', 5, 12345678, 1, 1),
('2025-02-01', '2025-03-30', 4.0, 4.3, '2025-1', 6, 1234567, 1, 1),
('2025-02-01', '2025-03-30', 4.0, 4.3, '2025-1', 7, 12345678, 1, 1);

-- Inserts para la tabla monitoring_monitor
INSERT INTO public.monitoring_monitor (monitoring_id, monitor_id) VALUES
(1, 'A00365713'),
(2, 'A00365713'),
(3, 'A00366522'),
(7, 'A00366522'),
(4, 'A00366522'),
(5, 'A00366522'),
(5, 'A00400122'),
(6, 'A00400122'),
(7, 'A00400122');

-- Inserts para la tabla activity
INSERT INTO public.activity (name, creation_date, finish_date, role_creator, role_responsable, category, description, monitoring_id, professor_id, monitor_id, state, delivery_date, semester, edited_date) VALUES
('Taller de Algoritmos', '2025-03-01', '2025-03-15', 'P', 'M', 'Educación', 'Ejercicios avanzados de algoritmia.', 1, 1234567, 'A00365713', 'PENDIENTE', '2025-03-15', '2025-1', NULL),
('Estudio de Caso', '2025-03-02', '2025-03-20', 'P', 'M', 'Investigación', 'Análisis de ejercicios.', 2, 12345678, 'A00400122', 'PENDIENTE', '2025-03-20', '2025-1', NULL);

-- Inserts para la tabla attendance
INSERT INTO public.attendance (activity_id, student_id) VALUES
(1, 'A00364725'),
(2, 'A00364813');

-- Inserts para la tabla department_head
INSERT INTO public.department_head (name, password) VALUES
('Norha', 'password'),
('Laura', 'password');

-- Inserts para la tabla head_professor
INSERT INTO public.head_professor (department_head_id, professor_id) VALUES
(1, 1234567),
(1, 12345678);
