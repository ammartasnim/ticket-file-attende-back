-- 1. Services
INSERT INTO service (name, avg_time, description) VALUES
                                                      ('Western Union', 12, 'Transfert d''argent international.'),
                                                      ('Rapid-Poste', 8, 'Envoi de courriers et colis express.'),
                                                      ('Mandat Minute', 10, 'Transfert d''argent instantané en Tunisie.'),
                                                      ('Versement CCP', 15, 'Dépôt d''espèces sur compte courant postal.'),
                                                      ('Retrait CCP', 10, 'Retrait d''espèces via chèque ou carte.'),
                                                      ('Épargne (CNE)', 12, 'Gestion des livrets d''épargne et retraits.'),
                                                      ('Colis Postaux', 20, 'Envoi et réception de colis nationaux/internationaux.'),
                                                      ('Paiement Factures', 5, 'Paiement STEG, SONEDE, et Télécom.'),
                                                      ('Mandat Organisme', 10, 'Paiement de bourses, retraites ou aides sociales.'),
                                                      ('Recharge e-Dinar', 4, 'Recharge de cartes à puce et comptes virtuels.');



-- 4. Clients (Added phone_number column)
INSERT INTO app_users (user_type, first_name, last_name, email, password, phone_number, role, is_active) VALUES
                                                                                                             ('CLIENT', 'Yassine', 'Mansour', 'yassine@gmail.com', '$2a$10$SrqgNc6MET3UO3WZi5RvkeO9wCeIp33/yssGhFjhlSqX33TJ3Ohhu', '55000001', 'CLIENT', true),
                                                                                                             ('CLIENT', 'Fatma', 'Karray', 'fatma@gmail.com', '$2a$10$SrqgNc6MET3UO3WZi5RvkeO9wCeIp33/yssGhFjhlSqX33TJ3Ohhu', '55000002', 'CLIENT', true),
                                                                                                             ('CLIENT', 'Omar', 'Dridi', 'omar@gmail.com', '$2a$10$SrqgNc6MET3UO3WZi5RvkeO9wCeIp33/yssGhFjhlSqX33TJ3Ohhu', '55000003', 'CLIENT', true),
                                                                                                             ('CLIENT', 'Salma', 'Jarraya', 'salma.j@gmail.com', '$2a$10$SrqgNc6MET3UO3WZi5RvkeO9wCeIp33/yssGhFjhlSqX33TJ3Ohhu', '55000004', 'CLIENT', true),
                                                                                                             ('CLIENT', 'Ahmed', 'Tounsi', 'ahmed.t@gmail.com', '$2a$10$SrqgNc6MET3UO3WZi5RvkeO9wCeIp33/yssGhFjhlSqX33TJ3Ohhu', '55000005', 'CLIENT', true),
                                                                                                             ('CLIENT', 'Ines', 'Belhaj', 'ines.bh@gmail.com', '$$2a$10$SrqgNc6MET3UO3WZi5RvkeO9wCeIp33/yssGhFjhlSqX33TJ3Ohhu', '55000006', 'CLIENT', true);
