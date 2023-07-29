CREATE TABLE account (
    uid            serial PRIMARY KEY,
    uuid           uuid  NOT NULL,
    username       text  NOT NULL,
    nickname       text,
    password       bytea NOT NULL,
    pang_balance   int   NOT NULL,
    cookie_balance int   NOT NULL,
    created_at     timestamptz DEFAULT NOW()
);

CREATE INDEX idx__account_uuid ON account (uuid);

CREATE TABLE achievement (
    iff_id                   int NOT NULL PRIMARY KEY,
    completed_with_milestone int NOT NULL,
    category                 int NOT NULL
);

CREATE TABLE achievement_milestone (
    iff_id             int  NOT NULL PRIMARY KEY,
    achievement_iff_id int  NOT NULL
        CONSTRAINT fk__achievement_milestone REFERENCES achievement (iff_id),
    name               text NOT NULL
);

CREATE TABLE player_character (
    uid          serial PRIMARY KEY,
    account_uid  int  NOT NULL
        CONSTRAINT fk_player_character__account REFERENCES account (uid) ON DELETE CASCADE,
    iff_id       int  NOT NULL,
    hair_color   int  NOT NULL,
    part_iff_ids json NOT NULL,
    part_uids    json NOT NULL,
    aux_parts    json NOT NULL,
    cutin_iff_id int  NOT NULL,
    stats        json NOT NULL,
    mastery      int  NOT NULL,
    cards        json NOT NULL
);

CREATE UNIQUE INDEX idx_player_character ON player_character (account_uid, iff_id);

CREATE TABLE player_caddie (
    uid         serial PRIMARY KEY,
    account_uid int NOT NULL
        CONSTRAINT fk_player_caddie__account REFERENCES account (uid) ON DELETE CASCADE,
    iff_id      int NOT NULL,
    level       int NOT NULL,
    experience  int NOT NULL
);
CREATE UNIQUE INDEX idx_player_caddie ON player_caddie (account_uid, iff_id);

CREATE TABLE player_inventory_item (
    uid         serial PRIMARY KEY,
    account_uid int NOT NULL
        CONSTRAINT fk_player_inventory_item__account REFERENCES account (uid) ON DELETE CASCADE,
    iff_id      int NOT NULL,
    quantity    int DEFAULT 0,
    stats       json
);

CREATE TABLE player_equipment (
    account_uid   int  NOT NULL PRIMARY KEY
        CONSTRAINT fk_player_statistics__account REFERENCES account (uid) ON DELETE CASCADE,
    item_iff_ids  json NOT NULL,
    character_uid int  NOT NULL,
    caddie_uid    int  NOT NULL,
    club_set_uid  int  NOT NULL,
    comet_iff_id  int  NOT NULL
);

CREATE TABLE player_statistics (
    account_uid               int    NOT NULL PRIMARY KEY
        CONSTRAINT fk_player_statistics__account REFERENCES account (uid) ON DELETE CASCADE,
    total_shots               int    NOT NULL,
    total_putts               int    NOT NULL,
    playtime_seconds          int    NOT NULL,
    shot_time_seconds         int    NOT NULL,
    longest_drive             float4 NOT NULL,
    pangya_shots              int    NOT NULL,
    timeouts                  int    NOT NULL,
    oob_shots                 int    NOT NULL,
    total_distance            int    NOT NULL,
    total_holes               int    NOT NULL,
    unfinished_holes          int    NOT NULL,
    hole_in_ones              int    NOT NULL,
    bunker_shots              int    NOT NULL,
    fairway_shots             int    NOT NULL,
    albatross                 int    NOT NULL,
    successful_putts          int    NOT NULL,
    longest_putt              float4 NOT NULL,
    longest_chip_in           float4 NOT NULL,
    experience                int    NOT NULL,
    level                     int    NOT NULL,
    pang_earned               bigint NOT NULL,
    total_score               int    NOT NULL,
    games_played              int    NOT NULL,
    game_combo_current_streak int    NOT NULL,
    game_combo_best_streak    int    NOT NULL,
    games_quit                int    NOT NULL,
    pang_won_in_battle        int    NOT NULL,
    pang_battle_wins          int    NOT NULL,
    pang_battle_losses        int    NOT NULL,
    pang_battle_all_ins       int    NOT NULL,
    pang_battle_combo         int    NOT NULL
);

CREATE TABLE player_achievement (
    uid         serial PRIMARY KEY,
    account_uid int NOT NULL
        CONSTRAINT fk_player_achievement__account REFERENCES account (uid) ON DELETE CASCADE,
    iff_id      int NOT NULL
);

CREATE UNIQUE INDEX idx_player_achievement ON player_achievement (account_uid, iff_id);

CREATE TABLE player_achievement_milestone (
    uid                    serial PRIMARY KEY,
    player_achievement_uid int NOT NULL
        CONSTRAINT fk_player_achievement_milestone__player_achievement REFERENCES player_achievement (uid) ON DELETE CASCADE,
    iff_id                 int NOT NULL,
    progress               INT NOT NULL DEFAULT 0,
    completed_at           timestamptz
);

CREATE UNIQUE INDEX idx_player_achievement_milestone ON player_achievement_milestone (player_achievement_uid, iff_id);