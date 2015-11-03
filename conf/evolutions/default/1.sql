#Users schema

# --- !Ups

create table USER(
  id SERIAL NOT NULL PRIMARY KEY,
  email VARCHAR NOT NULL
);

# --- !Downs

drop table USER;