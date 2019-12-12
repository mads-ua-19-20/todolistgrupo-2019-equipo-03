/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('1', 'domingo@ua', 'Domingo Gallardo', '123', '2001-02-10', true, false);
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('2', 'a@ua', 'alex', '123', '2001-02-11', false, false);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite, archivada) VALUES('1', 'Lavar coche', '1', '1', null, false);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite, archivada) VALUES('2', 'Renovar DNI', '1', '2', null, false);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite, archivada) VALUES('3', 'Lavar moto', '2', '1', null, false);
INSERT INTO tareas (id, titulo, usuario_id, estado, fechalimite, archivada) VALUES('4', 'Renovar Pasaporte', '2', '1', null, false);
INSERT INTO equipos (id, nombre) VALUES('2', 'Proyecto Adamantium');
INSERT INTO equipos (id, nombre) VALUES('1', 'Proyecto Cobalto');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '2');
INSERT INTO tareasequipo (id, titulo, equipo_id, estado, fechalimite, archivada, usuario_id) VALUES('1', 'Limpieza almacén', '1', '1', null, false, 1);
INSERT INTO tareasequipo (id, titulo, equipo_id, estado, fechalimite, archivada, usuario_id) VALUES('2', 'Pintar local', '1', '1', null, false, 2);
INSERT INTO tareasequipo (id, titulo, equipo_id, estado, fechalimite, archivada, usuario_id) VALUES('3', 'Montar muebles', '1', '1', null, false, null);
INSERT INTO tareasequipo (id, titulo, equipo_id, estado, fechalimite, archivada, usuario_id) VALUES('4', 'Arreglar cortacesped', '2', '1', null, false, null);