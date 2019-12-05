/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('1', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10', true, false);
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('2', 'pepe.garcia@gmail.com', 'Pepe García', '12345678', '2001-02-10', false, true);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite) VALUES('1', 'Lavar coche', '1', '1', null);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite) VALUES('2', 'Renovar DNI', '1', '1', null);
INSERT INTO equipos (id, nombre) VALUES('1', 'Proyecto Cobalto');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');
INSERT INTO equipos (id, nombre) VALUES('2', 'Proyecto Adamantium');

INSERT INTO tareasequipo (id, titulo, equipo_id, estado) VALUES('1', 'Limpieza almacén', '1', '1');