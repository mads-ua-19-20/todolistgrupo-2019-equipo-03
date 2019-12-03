/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('1', 'domingo@ua', 'Domingo Gallardo', '123', '2001-02-10', true, false);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite) VALUES('1', 'Lavar coche', '1', '1', null);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite) VALUES('2', 'Renovar DNI', '1', '2', null);
INSERT INTO equipos (id, nombre) VALUES('1', 'Proyecto Cobalto');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');
INSERT INTO equipos (id, nombre) VALUES('2', 'Proyecto Adamantium');