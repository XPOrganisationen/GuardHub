USE ghdb;

INSERT INTO cities (city_name) VALUES
    ('Copenhagen'),
    ('Roskilde'),
    ('Odense'),
    ('Aarhus'),
    ('Aalborg'),
    ('Esbjerg');

INSERT INTO certificates (certificate_title) VALUES
    ('First Aid'),
    ('CPR'),
    ('Fire Safety'),
    ('Crowd Control'),
    ('Active Shooter Response'),
    ('Site Patrol'),
    ('K9 Handling');

INSERT INTO users (name, email, password, phone_number, user_type) VALUES
    ('Alice Morgan','alice.morgan@example.com','$2b$12$adminhash1','+12125550101','ADMIN'),
    ('Bob Rivera','bob.rivera@example.com','$2b$12$guardhash1','+12125550202','GUARD'),
    ('Carlos Diaz','carlos.diaz@example.com','$2b$12$guardhash2','+12125550303','GUARD'),
    ('Diana Lee','diana.lee@example.com','$2b$12$guardhash3','+12125550404','GUARD'),
    ('Ethan Patel','ethan.patel@example.com','$2b$12$guardhash4','+12125550505','GUARD'),
    ('Fiona Zhang','fiona.zhang@example.com','$2b$12$guardhash5','+12125550606','GUARD');

INSERT INTO clients (name, email, city, address) VALUES
    ('Harbor Mall','contact@harbormall.com','Copenhagen', 'Havnepladsen 100, 1058 København, Denmark'),
    ('Sunset Film Studios','bookings@sunsetstudios.com','Roskilde','Sunsetvej 45, 4000 Roskilde, Denmark'),
    ('Lakeview Condos','manager@lakeviewcondos.com','Odense','Søpromenaden 200, 5000 Odense, Denmark'),
    ('Riverside Hospital','hrsecurity@riversidehosp.org','Copenhagen','Roskildevej 12, 2500 Valby (København), Denmark'),
    ('Oceanfront Resort','events@oceanfrontresort.com','Aarhus','Strandvejen 500, 8000 Aarhus C, Denmark');

INSERT INTO guard_certificates (guard_id, certificate_id) VALUES
    (2,1), -- Bob: First Aid
    (2,2), -- Bob: CPR
    (2,6), -- Bob: Site Patrol
    (3,1), -- Carlos: First Aid
    (3,4), -- Carlos: Crowd Control
    (3,6), -- Carlos: Site Patrol
    (4,2), -- Diana: CPR
    (4,3), -- Diana: Fire Safety
    (4,5), -- Diana: Active Shooter Response
    (5,6), -- Ethan: Site Patrol
    (5,7), -- Ethan: K9 Handling
    (6,1), -- Fiona: First Aid
    (6,2), -- Fiona: CPR
    (6,4); -- Fiona: Crowd Control

INSERT INTO shifts (client_id, title, shift_start, shift_end, description, required_guards) VALUES
    (1, 'Harbor Mall Night Patrol', '2026-05-15 22:00:00', '2026-05-16 06:00:00', 'Night patrol of mall, focus on entrances and parking areas.', 3),
    (2, 'Studio Premiere Crowd Control', '2026-05-18 17:00:00', '2026-05-18 23:30:00', 'Manage crowd at studio premiere event; public-facing duties.', 6),
    (3, 'Lakeview Building Reception', '2026-05-14 07:00:00', '2026-05-14 15:00:00', 'Day shift at condo reception and lobby monitoring.', 1),
    (4, 'Riverside ER Quick Response', '2026-05-16 00:00:00', '2026-05-16 08:00:00', 'Security presence in ER; occasional patient safety assistance.', 2),
    (5, 'Oceanfront Resort VIP Event', '2026-05-20 18:00:00', '2026-05-21 02:00:00', 'VIP event with high-profile guests; elevated security.', 4);

INSERT INTO required_certificates (shift_id, certificate_id) VALUES
-- Harbor Mall: First Aid (1), Site Patrol (6)
    (1,6),
    (1,1),
-- Studio Premiere Crowd Control: Crowd Control (4), CPR (2)
    (2,4),
    (2,2),
-- Lakeview Reception: First Aid (1)
    (3,1),
-- Riverside ER: CPR (2), First Aid (1)
    (4,2),
    (4,1),
-- Oceanfront VIP Event: Crowd Control (4), Fire Safety (3), Active Shooter Response (5)
    (5,4),
    (5,3),
    (5,5);

INSERT INTO shift_registrations (guard_id, shift_id, registration_status) VALUES
    (2,1,'PENDING'),   -- Bob registers for Harbor Mall
    (3,1,'PENDING'),   -- Carlos registers for Harbor Mall
    (6,1,'APPROVED'),  -- Fiona already approved for Harbor Mall
    (4,2,'PENDING'),   -- Diana registers for Studio Premiere
    (6,2,'REJECTED'),  -- Fiona applied but rejected (maybe lack of capacity)
    (5,2,'APPROVED'),  -- Ethan approved for Studio Premiere
    (3,3,'APPROVED'),  -- Carlos approved for Lakeview Reception
    (2,4,'PENDING'),   -- Bob for Riverside ER
    (4,4,'APPROVED'),  -- Diana approved for Riverside ER
    (5,5,'PENDING'),   -- Ethan for Oceanfront VIP
    (6,5,'PENDING');   -- Fiona for Oceanfront VIP