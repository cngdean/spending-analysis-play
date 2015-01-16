# --- !Ups
CREATE TABLE categorized_txns (
id varchar,
category varchar);

# --- !Downs
DROP TABLE IF EXISTS categorized_txns;