-- Insertar categorías
INSERT INTO ticket_category (category_name) VALUES ('Soporte Técnico');
INSERT INTO ticket_category (category_name) VALUES ('Facturación');
INSERT INTO ticket_category (category_name) VALUES ('Consultas Generales');
INSERT INTO ticket_category (category_name) VALUES ('Reclamos');
INSERT INTO ticket_category (category_name) VALUES ('Mejoras y Sugerencias');

-- Insertar tickets usando la secuencia
INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('No puedo ingresar', 'Error 403 al intentar loguearme.', 'ABIERTO', 1, 1, NOW());

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Problemas con la factura', 'La factura tiene un monto erróneo.', 'CERRADO', 1, 2, NOW() - 2);

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Consulta sobre producto', '¿Cómo funciona el nuevo servicio?', 'ABIERTO', 2, 3, NOW() - 1);

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Reclamo por atención', 'Fui mal atendido por soporte.', 'CERRADO', 3, 4, NOW() - 4);

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Sugerencia de mejora', 'Agreguen más opciones en el menú.', 'ABIERTO', 4, 5, NOW() - 3);

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Error en app móvil', 'Se cierra sola al iniciar.', 'ABIERTO', 4, 1, NOW() - 1);

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Consulta general', '¿Cuáles son los métodos de pago?', 'CERRADO', 5, 3, NOW() - 2);

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Error de sistema', 'Pantalla en blanco al acceder.', 'ABIERTO', 6, 1, NOW());

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Problema con boleta', 'No me llegó el comprobante.', 'CERRADO', 7, 2, NOW() - 1);

INSERT INTO ticket (title, ticket_description, status, client_id, category_id, created_at)
VALUES ('Sugerencia de funcionalidad', 'Incluir historial de compras.', 'ABIERTO', 2, 5, NOW() - 1);

COMMIT;
