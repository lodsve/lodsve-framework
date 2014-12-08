-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS t_account;
CREATE TABLE t_account (
  pk_id int(11) NOT NULL,
  login_name varchar(16) NOT NULL,
  password varchar(50) NOT NULL,
  create_date timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (pk_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_account_role
-- ----------------------------
DROP TABLE IF EXISTS t_account_role;
CREATE TABLE t_account_role (
  pk_id int(11) NOT NULL,
  account varchar(16) NOT NULL,
  role_code varchar(10) NOT NULL,
  PRIMARY KEY (pk_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role (
  pk_id int(11) NOT NULL,
  role_code varchar(10) NOT NULL,
  create_time timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (pk_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;