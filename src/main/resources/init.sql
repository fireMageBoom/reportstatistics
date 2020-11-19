DROP TABLE IF EXISTS `reportstatistic_jwt`;

CREATE TABLE IF NOT EXISTS `reportstatistic_jwt` (
  `user_id` int(8) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(32) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `is_delete` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;