drop table if exists to_dos;
create table to_dos (
   id SERIAL PRIMARY KEY,
   description varchar(255),
   username varchar(255)
);
