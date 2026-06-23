-- ============================================
-- Script de poblacion de BD (datos de prueba)
-- Sistema de Reservas de Salas
-- PostgreSQL
-- ============================================
-- Importante:
--  * Orden de insercion: padres antes que hijos (por las FKs)
--  * Se ejecuta DESPUES de que Hibernate cree el schema
--    (gracias a spring.jpa.defer-datasource-initialization=true)
--  * ON CONFLICT DO NOTHING: el script es idempotente,
--    se puede correr varias veces sin fallar por PK duplicada.
--  * Al final se sincronizan las secuencias IDENTITY con los MAX(id)
--    para que Hibernate siga insertando sin chocar con los id 1..5.

-- ============================================
-- 1. CARRERA
-- ============================================
INSERT INTO CARRERA (id, nombre_carrera, facultad) VALUES
(1, 'Ingenieria en Informatica',  'Facultad de Ingenieria'),
(2, 'Ingenieria Civil',          'Facultad de Ingenieria'),
(3, 'Medicina',                  'Facultad de Ciencias Medicas'),
(4, 'Derecho',                   'Facultad de Ciencias Juridicas'),
(5, 'Administracion de Empresas','Facultad de Economia y Negocios')
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 2. EDIFICIO
-- ============================================
INSERT INTO EDIFICIO (id, nombre_edificio, direccion) VALUES
(1, 'Biblioteca Central', 'Campus Central'),
(2, 'Edificio B', 'Av. Universidad 1100'),
(3, 'Edificio C', 'Av. Universidad 1200')
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 3. ESTUDIANTE  (FK id_carrera -> CARRERA)
-- ============================================
INSERT INTO ESTUDIANTE
(id, rut, nombre, apellido, correo, telefono, fecha_registro, id_carrera, password)
VALUES

(1,'11.111.111-1','Sujeto','de prueba','sujeto.prueba@uss.cl','+56911111111','2026-01-01',1,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(2,'18.123.456-7','Valentina','Soto','valentina.soto@uss.cl','+56911111112','2026-01-05',1,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(3,'17.234.567-8','Diego','Herrera','diego.herrera@uss.cl','+56911111113','2026-01-10',2,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(4,'19.345.678-9','Fernanda','Diaz','fernanda.diaz@uss.cl','+56911111114','2026-01-15',3,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(5,'16.456.789-0','Javiera','Torres','javiera.torres@uss.cl','+56911111115','2026-01-20',1,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(6,'15.567.890-1','Martin','Rojas','martin.rojas@uss.cl','+56911111116','2026-01-25',2,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(7,'14.678.901-2','Paula','Castillo','paula.castillo@uss.cl','+56911111117','2026-02-01',3,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(8,'13.789.012-3','Sebastian','Morales','sebastian.morales@uss.cl','+56911111118','2026-02-05',1,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(9,'12.890.123-4','Daniela','Vega','daniela.vega@uss.cl','+56911111119','2026-02-10',4,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(10,'11.901.234-5','Andres','Contreras','andres.contreras@uss.cl','+56911111120','2026-02-15',5,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(11,'10.012.345-6','Camila','Gonzalez','camila.gonzalez@uss.cl','+56911111121','2026-02-20',1,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(12,'20.123.456-7','Felipe','Munoz','felipe.munoz@uss.cl','+56911111122','2026-02-25',2,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(13,'21.234.567-8','Maria','Perez','maria.perez@uss.cl','+56911111123','2026-03-01',3,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(14,'22.345.678-9','Juan','Lopez','juan.lopez@uss.cl','+56911111124','2026-03-05',1,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO'),

(15,'23.456.789-0','Nicolas','Fuentes','nicolas.fuentes@uss.cl','+56911111125','2026-03-10',5,
'$2a$10$gLOPS30FomD3ZmIjNpYXgu7sVhowegagkiR4sVEYItG4CW7clulWO')

ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 4. SALA  (FK id_edificio -> EDIFICIO)
-- ============================================
INSERT INTO SALA (id, codigo_sala, nombre_sala, capacidad, piso, descripcion, estado, id_edificio) VALUES
(1, 'A101', 'Sala A101', 6, 1, 'Sala de estudio grupal', 'Disponible', 1),
(2, 'B202', 'Sala B202', 10, 2, 'Sala multimedia', 'Disponible', 1),
(3, 'C303', 'Sala C303', 4, 3, 'Sala individual', 'Disponible', 1),
(4, 'D404', 'Sala D404', 8, 4, 'Sala de estudio grupal', 'Disponible', 1),
(5, 'E505', 'Sala E505', 8, 5, 'Sala de estudio grupal', 'Disponible', 1),
(6, 'F606', 'Sala F606', 14, 6, 'Sala de estudio grupal', 'Disponible', 1)
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 5. HORARIO_DISPONIBLE  (FK id_sala -> SALA)
-- ============================================
INSERT INTO HORARIO_DISPONIBLE (id, hora_inicio, hora_termino, id_sala) VALUES
(1, '08:00:00', '09:30:00', 1),
(2, '09:30:00', '11:00:00', 1),
(3, '11:00:00', '12:30:00', 1),
(4, '14:00:00', '15:30:00', 1),
(5, '15:30:00', '17:00:00', 1),
(6, '17:00:00', '18:30:00', 1),
(7, '18:30:00', '20:00:00', 1),
(8, '20:00:00', '22:00:00', 1)
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 6. ESTADO_RESERVA
-- PK: id_estado (no "id" como el resto)
-- ============================================
INSERT INTO ESTADO_RESERVA (id_estado, nombre_estado) VALUES
(1, 'Confirmada'),
(2, 'Pendiente'),
(3, 'Cancelada'),
(4, 'Completada')
ON CONFLICT (id_estado) DO NOTHING;

-- ============================================
-- 7. RESERVA
--    FK id_estudiante -> ESTUDIANTE
--    FK id_sala       -> SALA
--    FK id_horario    -> HORARIO_DISPONIBLE
--    FK id_estado     -> ESTADO_RESERVA
-- ============================================
INSERT INTO RESERVA (id, fecha_reserva, observacion, fecha_creacion, id_estudiante, id_sala, id_horario, id_estado) VALUES
(1,'2026-06-22','Trabajo de proyecto','2026-06-20 10:00:00',2,1,1,1),
(2,'2026-06-22','Estudio grupal','2026-06-20 10:05:00',3,1,3,2),
(3,'2026-06-22','Preparacion examen','2026-06-20 10:10:00',4,1,5,1),

(4,'2026-06-23','Trabajo de proyecto','2026-06-21 10:00:00',5,1,2,1),
(5,'2026-06-23','Investigacion','2026-06-21 10:05:00',6,1,4,2),
(6,'2026-06-23','Reunion equipo','2026-06-21 10:10:00',7,1,7,1),

(7,'2026-06-24','Trabajo de proyecto','2026-06-22 10:00:00',8,1,1,2),
(8,'2026-06-24','Estudio grupal','2026-06-22 10:05:00',9,1,3,1),
(9,'2026-06-24','Preparacion examen','2026-06-22 10:10:00',10,1,6,2),

(10,'2026-06-25','Trabajo de proyecto','2026-06-23 10:00:00',11,1,2,1),
(11,'2026-06-25','Investigacion','2026-06-23 10:05:00',12,1,4,2),
(12,'2026-06-25','Reunion equipo','2026-06-23 10:10:00',13,1,8,1),

(13,'2026-06-26','Estudio grupal','2026-06-24 10:00:00',14,1,1,1),
(14,'2026-06-26','Preparacion examen','2026-06-24 10:05:00',15,1,5,2),
(15,'2026-06-26','Trabajo de proyecto','2026-06-24 10:10:00',2,1,7,1),

(16,'2026-06-29','Investigacion','2026-06-27 10:00:00',3,1,2,2),
(17,'2026-06-29','Reunion equipo','2026-06-27 10:05:00',4,1,4,1),
(18,'2026-06-29','Estudio grupal','2026-06-27 10:10:00',5,1,6,2),

(19,'2026-06-30','Trabajo de proyecto','2026-06-28 10:00:00',6,1,1,1),
(20,'2026-06-30','Preparacion examen','2026-06-28 10:05:00',7,1,3,2),
(21,'2026-06-30','Investigacion','2026-06-28 10:10:00',8,1,5,1),

(22,'2026-07-01','Trabajo de proyecto','2026-06-29 10:00:00',9,1,2,1),
(23,'2026-07-01','Reunion equipo','2026-06-29 10:05:00',10,1,4,2),
(24,'2026-07-01','Estudio grupal','2026-06-29 10:10:00',11,1,7,1),

(25,'2026-07-02','Preparacion examen','2026-06-30 10:00:00',12,1,1,2),
(26,'2026-07-02','Trabajo de proyecto','2026-06-30 10:05:00',13,1,5,1),
(27,'2026-07-02','Investigacion','2026-06-30 10:10:00',14,1,8,2),

(28,'2026-07-03','Reunion equipo','2026-07-01 10:00:00',15,1,2,1),
(29,'2026-07-03','Estudio grupal','2026-07-01 10:05:00',2,1,4,2),
(30,'2026-07-03','Trabajo de proyecto','2026-07-01 10:10:00',3,1,6,1)
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- Sincronizar secuencias IDENTITY
-- Hibernate usa IDENTITY (GENERATED BY DEFAULT AS IDENTITY en PG).
-- Como insertamos con id explicito, las secuencias quedan atras
-- y los siguientes inserts autogenerados chocarian con los id 1..5.
-- COALESCE maneja el caso "tabla vacia" -> setval(..., 1, false)
-- y el caso "tabla con datos" -> setval(MAX(id), true)
-- ============================================
SELECT setval(
  pg_get_serial_sequence('CARRERA',           'id'),
  COALESCE((SELECT MAX(id)        FROM CARRERA),           1),
  (SELECT COUNT(*) FROM CARRERA) > 0
);
SELECT setval(
  pg_get_serial_sequence('EDIFICIO',          'id'),
  COALESCE((SELECT MAX(id)        FROM EDIFICIO),          1),
  (SELECT COUNT(*) FROM EDIFICIO) > 0
);
SELECT setval(
  pg_get_serial_sequence('ESTUDIANTE',        'id'),
  COALESCE((SELECT MAX(id)        FROM ESTUDIANTE),        1),
  (SELECT COUNT(*) FROM ESTUDIANTE) > 0
);
SELECT setval(
  pg_get_serial_sequence('SALA',              'id'),
  COALESCE((SELECT MAX(id)        FROM SALA),              1),
  (SELECT COUNT(*) FROM SALA) > 0
);
SELECT setval(
  pg_get_serial_sequence('HORARIO_DISPONIBLE','id'),
  COALESCE((SELECT MAX(id)        FROM HORARIO_DISPONIBLE),1),
  (SELECT COUNT(*) FROM HORARIO_DISPONIBLE) > 0
);
SELECT setval(
  pg_get_serial_sequence('ESTADO_RESERVA',    'id_estado'),
  COALESCE((SELECT MAX(id_estado) FROM ESTADO_RESERVA),    1),
  (SELECT COUNT(*) FROM ESTADO_RESERVA) > 0
);
SELECT setval(
  pg_get_serial_sequence('RESERVA',           'id'),
  COALESCE((SELECT MAX(id)        FROM RESERVA),           1),
  (SELECT COUNT(*) FROM RESERVA) > 0
);
