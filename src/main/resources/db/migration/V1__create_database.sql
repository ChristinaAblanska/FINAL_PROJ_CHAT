-- academy_chat.user definition

CREATE TABLE IF NOT EXISTS user (
  id bigint NOT NULL AUTO_INCREMENT,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  email varchar(100) NOT NULL,
  user_name varchar(100) NOT NULL,
  password varchar(100) NOT NULL,
  status varchar(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY user_unique (email),
  UNIQUE KEY user_unique_1 (user_name)
);

-- academy_chat.message definition

CREATE TABLE IF NOT EXISTS message (
  id bigint NOT NULL AUTO_INCREMENT,
  content varchar(255) NOT NULL,
  sender_id bigint NOT NULL,
  receiver_id bigint NOT NULL,
  status varchar(100) NOT NULL,
  time_stamp datetime DEFAULT NULL,
  PRIMARY KEY (id),
  KEY message_user_FK (sender_id),
  KEY message_user_FK_1 (receiver_id)
);