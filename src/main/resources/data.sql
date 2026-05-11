USE ghdb;

INSERT INTO clients (client_name, client_email, client_phone_number, client_city, client_address) VALUES
    ('Novo Nordic', 'novo@nordic.com', '12131415', 'Copenhagen', 'hjulvej 25'),
    ('Roskilde Festival', 'roskilde@festival.com', '23242526', 'Roskilde', 'pindevej 36'),
    ('Coloplast', 'colo@plast.com', '34353637', 'Aalborg', 'duevej 40'),
    ('McDonalds', 'mc1@donalds.com', '45464748', 'Copenhagen', 'vejrvej 92'),
    ('McDonalds', 'mc2@donalds.com', '56575859', 'Aarhus', 'flodvej 22');

INSERT INTO Cities (city) VALUES
    ('Copenhagen'),
    ('Roskilde'),
    ('Odense'),
    ('Aarhus'),
    ('Aalborg'),
    ('Esbjerg');
