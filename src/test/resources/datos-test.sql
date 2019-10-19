/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('1', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10', true, false);
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('2', 'pepe.garcia@gmail.com', 'Pepe García', '12345678', '2001-02-10', false, true);
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');
