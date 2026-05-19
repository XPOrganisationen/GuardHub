USE ghdb;

INSERT INTO cities (city_name) VALUES
    ('Copenhagen'),
    ('Roskilde'),
    ('Odense'),
    ('Aarhus'),
    ('Aalborg'),
    ('Esbjerg');

INSERT INTO users (name, email, password, phone_number, user_type) VALUES
    ('Alice Morgan','alice.morgan@example.com','$2b$12$adminhash1','+12125550101','ADMIN'),
    ('Bob Rivera','bob.rivera@example.com','$2b$12$guardhash1','+12125550202','GUARD'),
    ('Carlos Diaz','carlos.diaz@example.com','$2b$12$guardhash2','+12125550303','GUARD'),
    ('Diana Lee','diana.lee@example.com','$2b$12$guardhash3','+12125550404','GUARD'),
    ('Ethan Patel','ethan.patel@example.com','$2b$12$guardhash4','+12125550505','GUARD'),
    ('Fiona Zhang','fiona.zhang@example.com','$2b$12$guardhash5','+12125550606','GUARD');

INSERT INTO clients (name, email, phone_number, city, address) VALUES
    ('Harbor Mall','contact@harbormall.com', '12345678','Copenhagen', 'Havnepladsen 100, 1058 København, Denmark'),
    ('Sunset Film Studios','bookings@sunsetstudios.com','87654321', 'Roskilde','Sunsetvej 45, 4000 Roskilde, Denmark'),
    ('Lakeview Condos','manager@lakeviewcondos.com','13245768','Odense','Søpromenaden 200, 5000 Odense, Denmark'),
    ('Riverside Hospital','hrsecurity@riversidehosp.org','86754231','Copenhagen','Roskildevej 12, 2500 Valby (København), Denmark'),
    ('Oceanfront Resort','events@oceanfrontresort.com','18273645','Aarhus','Strandvejen 500, 8000 Aarhus C, Denmark');

INSERT INTO shifts (client_id, title, shift_start, shift_end, description, food_served, parking_instructions, required_guards) VALUES
    (1, 'Harbor Mall Night Patrol', '2026-05-15 22:00:00', '2026-05-16 06:00:00', 'Night patrol of mall, focus on entrances and parking areas.', false, 'Free. Use our parking garage at the site of the shift. Your boss knows the code for the gate.', 3),
    (2, 'Studio Premiere Crowd Control', '2026-05-18 17:00:00', '2026-05-18 23:30:00', 'Manage crowd at studio premiere event; public-facing duties.', true, 'None', 6),
    (3, 'Lakeview Building Reception', '2026-05-14 07:00:00', '2026-05-14 15:00:00', 'Day shift at condo reception and lobby monitoring.', true, 'The library opposite the condo is not too crowded, try that.', 1),
    (4, 'Riverside ER Quick Response', '2026-05-16 00:00:00', '2026-05-16 08:00:00', 'Security presence in ER; occasional patient safety assistance.', true, 'None', 2),
    (5, 'Oceanfront Resort VIP Event', '2026-05-20 18:00:00', '2026-05-21 02:00:00', 'VIP event with high-profile guests; elevated security.', false, 'There is an employee parking lot. We know your license plate numbers, so roll on in.', 4);

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
