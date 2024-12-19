/* Adding example data to the players table */
INSERT INTO players (name, birthdate)
VALUES ('VIVIAN', TO_DATE('12-03-2000', 'DD-MM-YYYY')),
       ('BOYAN', TO_DATE('06-07-89', 'DD-MM-YY')),
       ('ALUN', TO_DATE('10-11-12', 'DD-MM-YY'));

/* Adding data for 2 example games */
INSERT INTO games (player_id,
                   board_size, score,
                   start, duration)
VALUES ((SELECT player_id
         FROM players
         WHERE name = 'VIVIAN'),
        7, 10,
        NOW(), '5 minutes'),
       ((SELECT player_id
         FROM players
         WHERE name = 'BOYAN'),
        5, 123,
        NOW(), '2 hours ');

/* Setting the end of a game */
UPDATE games
SET "end" = NOW() + '2 hours'
WHERE player_id = (SELECT player_id
                   FROM players
                   WHERE name = 'BOYAN');

/* Creating an example board of tiles for a game */
INSERT INTO tiles (x, y, is_ship, is_revealed, game_id)
VALUES (1, 'A', FALSE, TRUE, 1),
       (2, 'A', FALSE, TRUE, 1),
       (3, 'A', FALSE, TRUE, 1),
       (4, 'A', FALSE, TRUE, 1),
       (5, 'A', FALSE, TRUE, 1),
       (6, 'A', FALSE, TRUE, 1),
       (7, 'A', FALSE, TRUE, 1),
       (1, 'B', FALSE, TRUE, 1),
       (2, 'B', TRUE, TRUE, 1),
       (3, 'B', FALSE, TRUE, 1),
       (4, 'B', TRUE, TRUE, 1),
       (5, 'B', FALSE, TRUE, 1),
       (6, 'B', TRUE, TRUE, 1),
       (7, 'B', FALSE, TRUE, 1),
       (1, 'C', FALSE, TRUE, 1),
       (2, 'C', TRUE, TRUE, 1),
       (3, 'C', FALSE, TRUE, 1),
       (4, 'C', FALSE, TRUE, 1),
       (5, 'C', FALSE, TRUE, 1),
       (6, 'C', TRUE, TRUE, 1),
       (7, 'C', FALSE, TRUE, 1),
       (1, 'D', FALSE, TRUE, 1),
       (2, 'D', TRUE, TRUE, 1),
       (3, 'D', FALSE, TRUE, 1),
       (4, 'D', FALSE, TRUE, 1),
       (5, 'D', FALSE, TRUE, 1),
       (6, 'D', FALSE, TRUE, 1),
       (7, 'D', FALSE, TRUE, 1),
       (1, 'E', FALSE, TRUE, 1),
       (2, 'E', FALSE, TRUE, 1),
       (3, 'E', FALSE, TRUE, 1),
       (4, 'E', TRUE, TRUE, 1),
       (5, 'E', FALSE, TRUE, 1),
       (6, 'E', FALSE, TRUE, 1),
       (7, 'E', FALSE, TRUE, 1),
       (1, 'F', FALSE, TRUE, 1),
       (2, 'F', TRUE, TRUE, 1),
       (3, 'F', FALSE, TRUE, 1),
       (4, 'F', FALSE, TRUE, 1),
       (5, 'F', FALSE, TRUE, 1),
       (6, 'F', FALSE, TRUE, 1),
       (7, 'F', FALSE, TRUE, 1),
       (1, 'G', FALSE, TRUE, 1),
       (2, 'G', FALSE, TRUE, 1),
       (3, 'G', FALSE, TRUE, 1),
       (4, 'G', FALSE, TRUE, 1),
       (5, 'G', FALSE, TRUE, 1),
       (6, 'G', TRUE, TRUE, 1),
       (7, 'G', TRUE, TRUE, 1);

/* Inserting the ships */
INSERT INTO ships (ship_type_id)
VALUES (2),
       (3),
       (3),
       (4),
       (4),
       (4);

/* Setting up the ships' placements */
INSERT INTO placements (game_id, ship_id, x, y, is_vertical)
VALUES (1, 1, 2, 'B', TRUE),
       (1, 2, 6, 'B', TRUE),
       (1, 3, 6, 'G', FALSE),
       (1, 4, 4, 'B', NULL),
       (1, 5, 4, 'E', NULL),
       (1, 6, 2, 'F', NULL);

/* Checking out if all the tables have data */
SELECT * FROM players;
SELECT * FROM games;
SELECT * FROM tiles;
SELECT * FROM ship_types;
SELECT * FROM ships;
SELECT * FROM placements;