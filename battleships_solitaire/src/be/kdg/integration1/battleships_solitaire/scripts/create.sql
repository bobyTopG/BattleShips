/* Dropping the database and the tables if they already exist */
-- DROP DATABASE IF EXISTS ascii2;
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS tiles CASCADE;
DROP TABLE IF EXISTS ship_types CASCADE;
DROP TABLE IF EXISTS ships CASCADE;
DROP TABLE IF EXISTS placements CASCADE;

/* Creating the database */
-- CREATE DATABASE ascii2;

/* Creating the PLAYERS table */
CREATE TABLE IF NOT EXISTS players (
    player_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_player_id PRIMARY KEY,
    name VARCHAR(12)
        CONSTRAINT nn_player_name NOT NULL
        CONSTRAINT uq_player_name UNIQUE
        CONSTRAINT ch_player_name
            CHECK ( name = UPPER(name) ),
    birthdate DATE
        CONSTRAINT nn_birthdate NOT NULL
        CONSTRAINT ch_birthdate
            CHECK ( birthdate < NOW() ),
    join_date DATE DEFAULT CURRENT_DATE
        CONSTRAINT nn_join_date NOT NULL
);

/* Creating the GAMES table */
CREATE TABLE IF NOT EXISTS games (
    game_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_game_id PRIMARY KEY,
    start TIMESTAMP(0)
        CONSTRAINT nn_start NOT NULL,
    "end" TIMESTAMP(0),
    duration INTERVAL(0)
        CONSTRAINT nn_duration NOT NULL,
    score NUMERIC(3)
        CONSTRAINT nn_score NOT NULL,
    board_size NUMERIC(2)
        CONSTRAINT nn_board_size NOT NULL
        CONSTRAINT ch_board_size
            CHECK ( board_size IN (5, 7, 9) ),
    player_id INTEGER
        CONSTRAINT nn_player_id NOT NULL
        CONSTRAINT fk_player_id REFERENCES games(game_id)
        ON DELETE SET NULL
);

/* Creating the TILES table */
CREATE TABLE IF NOT EXISTS tiles (
    tile_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_tile_id PRIMARY KEY,
    x NUMERIC(2)
        CONSTRAINT nn_x NOT NULL,
    y NUMERIC(2)
        CONSTRAINT nn_y NOT NULL,
    is_ship BOOLEAN,
    game_id INTEGER
        CONSTRAINT nn_game_id NOT NULL
        CONSTRAINT fk_tile_game_id REFERENCES games(game_id)
        ON DELETE CASCADE
);

/* Creating the SHIP_TYPES table */
CREATE TABLE IF NOT EXISTS ship_types (
    ship_type_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_ship_type_id PRIMARY KEY,
    length NUMERIC(1)
        CONSTRAINT nn_length NOT NULL,
    name VARCHAR(12)
        CONSTRAINT nn_ship_name NOT NULL
);

/* Creating the SHIPS table */
CREATE TABLE IF NOT EXISTS ships (
    ship_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_ship_id PRIMARY KEY,
    ship_type_id INTEGER
        CONSTRAINT nn_ship_type_id NOT NULL
        CONSTRAINT fk_ship_type_id REFERENCES ship_types(ship_type_id)
        ON DELETE SET NULL
);

/* Creating the PLACEMENTS table */
CREATE TABLE IF NOT EXISTS placements (
    game_id INTEGER
        CONSTRAINT nn_placement_game_id NOT NULL
        CONSTRAINT fk_game_id REFERENCES games(game_id),
    ship_id INTEGER
        CONSTRAINT nn_placement_ship_id NOT NULL
        CONSTRAINT fk_ship_id REFERENCES ships(ship_id),
    x NUMERIC(2)
        CONSTRAINT nn_ship_x NOT NULL,
    y NUMERIC(2)
        CONSTRAINT nn_ship_y NOT NULL,
    is_vertical BOOLEAN,
    CONSTRAINT pk_placements PRIMARY KEY (game_id, ship_id)
);