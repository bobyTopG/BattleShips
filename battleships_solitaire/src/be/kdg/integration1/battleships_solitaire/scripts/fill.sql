/* Adding example data to the players table */
INSERT INTO players (name)
VALUES ('VIVIAN'),
       ('BOYAN'),
       ('ALUN');

/* Adding data for 2 example games */
INSERT INTO games (player_id, board_size, score, start, duration)
VALUES ((SELECT player_id FROM players WHERE name = 'VIVIAN'), 7, 10, NOW(), '5 mins'),
       ((SELECT player_id FROM players WHERE name = 'BOYAN'), 5, 123, NOW(), '2 hours ');

/* Setting the end of a game */
UPDATE games SET "end" = NOW() + '2 hours' WHERE player_id = (SELECT player_id FROM players WHERE name = 'BOYAN');

/*  */


SELECT * FROM players;
SELECT * FROM games;
TRUNCATE players RESTART IDENTITY;
TRUNCATE games RESTART IDENTITY CASCADE;