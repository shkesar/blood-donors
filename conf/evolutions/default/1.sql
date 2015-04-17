# --- !Ups
CREATE TABLE donors (
  name varchar(255) NOT NULL,
  address varchar(255) NOT NULL,
  contact varchar(255) NOT NULL,
  bloodGroup varchar(255) NOT NULL,
  locality varchar(255) NOT NULL,
  city varchar(255) NOT NULL,
  PRIMARY KEY (name)
)

# --- !Downs
DROP TABLE donors
