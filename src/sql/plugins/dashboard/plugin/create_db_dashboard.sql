
--
-- Structure for table dashboard_publicdashboard
--

DROP TABLE IF EXISTS dashboard_publicdashboard;
CREATE TABLE dashboard_publicdashboard (
id_public_user_profile int AUTO_INCREMENT,
typedashboard varchar(255) default '' NOT NULL,
position int default '0' NOT NULL,
location long varchar,
PRIMARY KEY (id_public_user_profile)
);
