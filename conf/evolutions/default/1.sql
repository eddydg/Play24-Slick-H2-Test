#Users schema

# --- !Ups

create table "USER"(
  user_id SERIAL NOT NULL PRIMARY KEY,
  user_email VARCHAR NOT NULL,
  user_password VARCHAR NOT NULL,
  is_super BOOLEAN NOT NULL,
  status VARCHAR NOT NULL
);

CREATE TABLE ADVERTISER(
  advertiser_id SERIAL NOT NULL PRIMARY KEY,
  advertiser_name VARCHAR NOT NULL
);

CREATE TABLE ADVERTISERACCESS(
  advertiser_access_id SERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES USER(user_id),
  advertiser_id BIGINT NOT NULL REFERENCES ADVERTISER(advertiser_id),
  FOREIGN KEY ("user_id") REFERENCES "USER"("user_id"),
  FOREIGN KEY ("advertiser_id") REFERENCES "ADVERTISER"("advertiser_id")
);

# --- !Downs

drop table USER;