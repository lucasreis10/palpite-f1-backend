-- Reset completo dos pilotos e associação com escuderias
-- Esta migration resolve o problema de checksum do Flyway

-- Limpar dados existentes
DELETE FROM pilots;

-- Popular todos os pilotos da temporada 2024 de F1 com dados completos
INSERT INTO pilots (driver_id, given_name, family_name, date_of_birth, nationality, url, permanent_number, code, active) VALUES
-- Red Bull Racing
('max_verstappen', 'Max', 'Verstappen', '1997-09-30', 'Dutch', 'http://en.wikipedia.org/wiki/Max_Verstappen', 1, 'VER', true),
('perez', 'Sergio', 'Pérez', '1990-01-26', 'Mexican', 'http://en.wikipedia.org/wiki/Sergio_P%C3%A9rez', 11, 'PER', true),

-- Ferrari
('leclerc', 'Charles', 'Leclerc', '1997-10-16', 'Monégasque', 'http://en.wikipedia.org/wiki/Charles_Leclerc', 16, 'LEC', true),
('sainz', 'Carlos', 'Sainz Jr.', '1994-09-01', 'Spanish', 'http://en.wikipedia.org/wiki/Carlos_Sainz_Jr.', 55, 'SAI', true),

-- Mercedes
('hamilton', 'Lewis', 'Hamilton', '1985-01-07', 'British', 'http://en.wikipedia.org/wiki/Lewis_Hamilton', 44, 'HAM', true),
('russell', 'George', 'Russell', '1998-02-15', 'British', 'http://en.wikipedia.org/wiki/George_Russell_(racing_driver)', 63, 'RUS', true),

-- McLaren
('norris', 'Lando', 'Norris', '1999-11-13', 'British', 'http://en.wikipedia.org/wiki/Lando_Norris', 4, 'NOR', true),
('piastri', 'Oscar', 'Piastri', '2001-04-06', 'Australian', 'http://en.wikipedia.org/wiki/Oscar_Piastri', 81, 'PIA', true),

-- Aston Martin
('alonso', 'Fernando', 'Alonso', '1981-07-29', 'Spanish', 'http://en.wikipedia.org/wiki/Fernando_Alonso', 14, 'ALO', true),
('stroll', 'Lance', 'Stroll', '1998-10-29', 'Canadian', 'http://en.wikipedia.org/wiki/Lance_Stroll', 18, 'STR', true),

-- Alpine F1 Team
('gasly', 'Pierre', 'Gasly', '1996-02-07', 'French', 'http://en.wikipedia.org/wiki/Pierre_Gasly', 10, 'GAS', true),
('ocon', 'Esteban', 'Ocon', '1996-09-17', 'French', 'http://en.wikipedia.org/wiki/Esteban_Ocon', 31, 'OCO', true),

-- Williams
('albon', 'Alexander', 'Albon', '1996-03-23', 'Thai', 'http://en.wikipedia.org/wiki/Alexander_Albon', 23, 'ALB', true),
('sargeant', 'Logan', 'Sargeant', '2000-12-31', 'American', 'http://en.wikipedia.org/wiki/Logan_Sargeant', 2, 'SAR', true),
('colapinto', 'Franco', 'Colapinto', '2003-05-27', 'Argentine', 'http://en.wikipedia.org/wiki/Franco_Colapinto', 43, 'COL', true),

-- RB F1 Team (AlphaTauri)
('tsunoda', 'Yuki', 'Tsunoda', '2000-05-11', 'Japanese', 'http://en.wikipedia.org/wiki/Yuki_Tsunoda', 22, 'TSU', true),
('ricciardo', 'Daniel', 'Ricciardo', '1989-07-01', 'Australian', 'http://en.wikipedia.org/wiki/Daniel_Ricciardo', 3, 'RIC', true),
('lawson', 'Liam', 'Lawson', '2002-02-11', 'New Zealand', 'http://en.wikipedia.org/wiki/Liam_Lawson', 30, 'LAW', true),

-- Kick Sauber (Alfa Romeo)
('bottas', 'Valtteri', 'Bottas', '1989-08-28', 'Finnish', 'http://en.wikipedia.org/wiki/Valtteri_Bottas', 77, 'BOT', true),
('zhou', 'Zhou', 'Guanyu', '1999-05-30', 'Chinese', 'http://en.wikipedia.org/wiki/Zhou_Guanyu', 24, 'ZHO', true),

-- Haas F1 Team
('magnussen', 'Kevin', 'Magnussen', '1992-10-05', 'Danish', 'http://en.wikipedia.org/wiki/Kevin_Magnussen', 20, 'MAG', true),
('hulkenberg', 'Nico', 'Hülkenberg', '1987-08-19', 'German', 'http://en.wikipedia.org/wiki/Nico_H%C3%BClkenberg', 27, 'HUL', true),

-- Pilotos de reserva/substitutos que participaram em 2024
('bearman', 'Oliver', 'Bearman', '2005-05-08', 'British', 'http://en.wikipedia.org/wiki/Oliver_Bearman', 38, 'BEA', true),
('doohan', 'Jack', 'Doohan', '2003-01-20', 'Australian', 'http://en.wikipedia.org/wiki/Jack_Doohan', 7, 'DOO', true);

-- Associar todos os pilotos às suas escuderias
-- Red Bull Racing
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'red_bull') 
WHERE driver_id IN ('max_verstappen', 'perez');

-- Ferrari
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'ferrari') 
WHERE driver_id IN ('leclerc', 'sainz', 'bearman');

-- Mercedes
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'mercedes') 
WHERE driver_id IN ('hamilton', 'russell');

-- McLaren
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'mclaren') 
WHERE driver_id IN ('norris', 'piastri');

-- Aston Martin
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'aston_martin') 
WHERE driver_id IN ('alonso', 'stroll');

-- Alpine F1 Team
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'alpine') 
WHERE driver_id IN ('gasly', 'ocon', 'doohan');

-- Williams
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'williams') 
WHERE driver_id IN ('albon', 'sargeant', 'colapinto');

-- RB F1 Team
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'rb') 
WHERE driver_id IN ('tsunoda', 'ricciardo', 'lawson');

-- Kick Sauber
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'kick_sauber') 
WHERE driver_id IN ('bottas', 'zhou');

-- Haas F1 Team
UPDATE pilots SET constructor_id = (SELECT id FROM constructors WHERE constructor_id = 'haas') 
WHERE driver_id IN ('magnussen', 'hulkenberg'); 