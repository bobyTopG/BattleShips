/* Dropping the database and the tables if they already exist */
-- DROP DATABASE IF EXISTS battleships;
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS tiles CASCADE;
DROP TABLE IF EXISTS ship_types CASCADE;
DROP TABLE IF EXISTS ships CASCADE;
DROP TABLE IF EXISTS placements CASCADE;

/* Creating the database */
-- CREATE DATABASE battleships;

/* Creating the PLAYERS table */
CREATE TABLE players (
    player_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_player_id PRIMARY KEY,
    name VARCHAR(12)
        CONSTRAINT nn_player_name NOT NULL
        CONSTRAINT uq_player_name UNIQUE
);

/* Creating the GAMES table */
CREATE TABLE games (
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
        CONSTRAINT nn_board_size NOT NULL,
    player_id INTEGER
        CONSTRAINT nn_player_id NOT NULL
        CONSTRAINT fk_player_id REFERENCES games(game_id)
);

/* Creating the TILES table */
CREATE TABLE tiles (
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
);

/* Creating the SHIP_TYPES table */
CREATE TABLE ship_types (
    ship_type_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_ship_type_id PRIMARY KEY,
    length NUMERIC(1)
        CONSTRAINT nn_length NOT NULL,
    name VARCHAR(12)
        CONSTRAINT nn_ship_name NOT NULL
);

/* Creating the SHIPS table */
CREATE TABLE ships (
    ship_id INTEGER
        GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_ship_id PRIMARY KEY,
    ship_type_id INTEGER
        CONSTRAINT nn_ship_type_id NOT NULL
        CONSTRAINT fk_ship_type_id REFERENCES ship_types(ship_type_id)
);

/* Creating the PLACEMENTS table */
CREATE TABLE placements (
    game_id INTEGER
        CONSTRAINT fk_game_id NOT NULL,
    ship_id INTEGER
        CONSTRAINT fk_ship_id NOT NULL,
    x NUMERIC(2)
        CONSTRAINT nn_ship_x NOT NULL,
    y NUMERIC(2)
        CONSTRAINT nn_ship_y NOT NULL,
    is_vertical BOOLEAN,
    CONSTRAINT pk_placements PRIMARY KEY (game_id, ship_id)
);