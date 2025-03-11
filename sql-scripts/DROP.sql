-- Eliminar tablas en orden
DROP TABLE IF EXISTS public.student_course CASCADE;
DROP TABLE IF EXISTS public.course_professor CASCADE;
DROP TABLE IF EXISTS public.attendance CASCADE;
DROP TABLE IF EXISTS public.activity CASCADE;
DROP TABLE IF EXISTS public.monitoring_monitor CASCADE;
DROP TABLE IF EXISTS public.monitoring CASCADE;
DROP TABLE IF EXISTS public.monitor CASCADE;
DROP TABLE IF EXISTS public.student CASCADE;
DROP TABLE IF EXISTS public.professor CASCADE;
DROP TABLE IF EXISTS public.head_professor CASCADE;
DROP TABLE IF EXISTS public.department_head CASCADE;
DROP TABLE IF EXISTS public.course CASCADE;
DROP TABLE IF EXISTS public.program CASCADE;
DROP TABLE IF EXISTS public.school CASCADE;
DROP TABLE IF EXISTS public.prospect CASCADE;

-- Opcional: Verificar si todas las tablas han sido eliminadas
SELECT tablename FROM pg_tables WHERE schemaname = 'public';
