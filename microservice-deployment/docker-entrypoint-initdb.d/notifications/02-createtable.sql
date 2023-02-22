CREATE TABLE IF NOT EXISTS eventlog
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    "action" VARCHAR(255)

);
