-- PostgreSQL database dump
--

-- Dumped from database version 16.6
-- Dumped by pg_dump version 17.0

-- Started on 2025-03-04 14:26:20

-- SET statement_timeout = 0;
-- SET lock_timeout = 0;
-- SET idle_in_transaction_session_timeout = 0;
-- SET transaction_timeout = 0;
-- SET client_encoding = 'UTF8';
-- SET standard_conforming_strings = on;
-- SELECT pg_catalog.set_config('search_path', '', false);
-- SET check_function_bodies = false;
-- SET xmloption = content;
-- SET client_min_messages = warning;
-- SET row_security = off;

--
-- Table structure for table: activity
--

CREATE TABLE public.activity (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date DATE NOT NULL,
    finish_date DATE NOT NULL,
    role_creator VARCHAR(10) NOT NULL,
    role_responsable VARCHAR(10) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    monitoring_id INTEGER NOT NULL,
    professor_id INTEGER NOT NULL,
    monitor_id VARCHAR(20) NOT NULL,
    state VARCHAR(20) NOT NULL,
    delivery_date DATE,
    semester VARCHAR(10) NOT NULL,
    edited_date DATE
);

--
-- Table structure for table: attendance
--

CREATE TABLE public.attendance (
    id SERIAL PRIMARY KEY,
    activity_id INTEGER NOT NULL,
    student_id VARCHAR(20) NOT NULL
);

--
-- Table structure for table: course
--

CREATE TABLE public.course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    program_id INTEGER NOT NULL
);

--
-- Table structure for table: course_professor
--

CREATE TABLE public.course_professor (
    id SERIAL PRIMARY KEY,
    course_id INTEGER NOT NULL,
    student_id VARCHAR(20) NOT NULL
);

--
-- Table structure for table: department_head
--

CREATE TABLE public.department_head (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

--
-- Table structure for table: head_professor
--

CREATE TABLE public.head_professor (
    id SERIAL PRIMARY KEY,
    department_head_id INTEGER NOT NULL,
    professor_id INTEGER NOT NULL
);

--
-- Table structure for table: monitor
--

CREATE TABLE public.monitor (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    semester INTEGER NOT NULL,
    grade_average DECIMAL(3,2) NOT NULL,
    grade_course DECIMAL(3,2) NOT NULL,
    email VARCHAR(255) NOT NULL
);

--
-- Table structure for table: monitoring
--

CREATE TABLE public.monitoring (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    finish_date DATE NOT NULL,
    average_grade DECIMAL(3,2) NOT NULL,
    course_grade DECIMAL(3,2) NOT NULL,
    semester VARCHAR(10) NOT NULL,
    course_id INTEGER NOT NULL,
    professor_id INTEGER NOT NULL,
    school_id INTEGER NOT NULL,
    program_id INTEGER NOT NULL
);

--
-- Table structure for table: monitoring_monitor
--

CREATE TABLE public.monitoring_monitor (
    id SERIAL PRIMARY KEY,
    monitoring_id INTEGER NOT NULL,
    monitor_id VARCHAR(20) NOT NULL
);

--
-- Table structure for table: professor
--

CREATE TABLE public.professor (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

--
-- Table structure for table: program
--

CREATE TABLE public.program (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    school_id INTEGER NOT NULL
);

--
-- Table structure for table: prospect
--

CREATE TABLE public.prospect (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    semester INTEGER NOT NULL,
    grade_average DECIMAL(3,2) NOT NULL,
    grade_course DECIMAL(3,2) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    id INTEGER NOT NULL UNIQUE
);

--
-- Table structure for table: school
--

CREATE TABLE public.school (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--
-- Table structure for table: student
--

CREATE TABLE public.student (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--
-- Table structure for table: student_course
--

CREATE TABLE public.student_course (
    id SERIAL PRIMARY KEY,
    course_id INTEGER NOT NULL,
    student_id VARCHAR(20) NOT NULL
);
