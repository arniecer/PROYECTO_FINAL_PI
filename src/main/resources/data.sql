-- ========================================
-- DATOS INICIALES DE GRANDSPICY
-- ========================================

-- Contraseñas encriptadas con BCrypt
-- admin / admin123
-- user  / user123

INSERT IGNORE INTO users (username, email, password, role, name, created_at) VALUES
('admin', 'admin@grandspicy.com', '$2a$10$ResYFColE.lsaRQRyF89Ee3hscZi.OhDhSEzRZtt6mLRPHrSMg4Mu', 'ADMIN', 'Administrador', NOW()),
('user', 'user@grandspicy.com', '$2a$10$iu5w51t0Dl8h02HRuaWVSOgKs9c.3sFyO0A7Vrx.mvhT6miOTexCC', 'USER', 'Usuario Demo', NOW());

INSERT IGNORE INTO products (name, description, price, image, category, scoville_level, country_of_origin, purchase_link, rating, created_at) VALUES
('Salsa Habanero Premium', 'Salsa artesanal elaborada con habaneros frescos cultivados en la península de Yucatán. Intensamente picante con un toque afrutado y cítrico.', 6.99, '1.webp', 'Salsas', 5000, 'México', 'https://www.amazon.es/s?k=salsa+habanero', 4.8, NOW()),
('Chile de Árbol Seco', 'Chiles de árbol seleccionados a mano, perfectos para salsas y guisos. Paquete de 200g. Originarios de Puebla.', 4.50, '2.webp', 'Chiles secos', 15000, 'México', 'https://www.amazon.es/s?k=chile+de+arbol', 4.2, NOW()),
('Salsa Ghost Pepper', 'La salsa más picante de nuestra colección. Elaborada con auténtico Bhut Jolokia (Ghost Pepper) de la India.', 8.99, '3.webp', 'Salsas', 1000000, 'India', 'https://www.amazon.es/s?k=ghost+pepper+salsa', 4.9, NOW()),
('Mix Chiles Picantes', 'Selección de chiles picantes variados de distintas regiones del mundo. Ideal para los amantes del picante extremo.', 5.99, '4.webp', 'Chiles secos', 50000, 'Varios', 'https://www.amazon.es/s?k=mix+chiles+picantes', 3.5, NOW()),
('Chips Picantes Extremas', 'Chips de maíz bañados en una mezcla secreta de especias picantes tailandesas. Bolsa de 300g.', 3.99, '5.webp', 'Snacks', 8000, 'Tailandia', 'https://www.amazon.es/s?k=chips+picantes', 4.0, NOW()),
('Salsa Jalapeño Natural', 'Salsa suave de jalapeño natural cultivado en España. Perfecta para quienes se inician en el mundo picante.', 5.50, '6.webp', 'Salsas', 2500, 'España', 'https://www.amazon.es/s?k=salsa+jalapeno', 4.6, NOW()),
('Cacahuate Super Spicy', 'Cacahuates crujientes con recubrimiento picante estilo coreano. Bolsa de 250g.', 4.25, '7.webp', 'Snacks', 6000, 'Corea del Sur', 'https://www.amazon.es/s?k=cacahuates+picantes', 4.3, NOW()),
('Snack Mix Picante', 'Mezcla de snacks con un toque picante irresistible. Incluye garbanzos, almendras y pretzels especiados.', 4.99, '8.webp', 'Snacks', 3500, 'EE.UU.', 'https://www.amazon.es/s?k=snacks+picantes', 3.8, NOW());

INSERT IGNORE INTO reviews (user_id, product_id, rating, comment, created_at) VALUES
(2, 1, 5, '¡Increíble salsa! Muy picante pero con mucho sabor. La recomiendo totalmente.', NOW()),
(2, 2, 4, 'Buenos chiles para cocinar, muy recomendados. Eso si, cuidado con la cantidad.', NOW()),
(2, 3, 5, 'Esto sí es picante de verdad. Me encanta el Ghost Pepper, no encontré nada igual.', NOW());
