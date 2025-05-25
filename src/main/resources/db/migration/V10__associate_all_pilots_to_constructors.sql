-- Associar todos os pilotos às suas escuderias (temporada 2024)

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