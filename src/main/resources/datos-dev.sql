/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin_check, bloqueado) VALUES('1', 'domingo@ua', 'Domingo Gallardo', '123', '2001-02-10', true, false);
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');
