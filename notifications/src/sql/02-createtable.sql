DROP TABLE IF EXISTS eventlog;
CREATE TABLE eventlog
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    "action" VARCHAR(255)
);
