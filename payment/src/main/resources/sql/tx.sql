CREATE DATABASE `tx` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `name` varchar(50) NOT NULL,
                        `sex` varchar(50) NOT NULL DEFAULT 'male',
                        `phone` varchar(50) NOT NULL,
                        `password` varchar(50) NOT NULL,
                        `balance` decimal(20,0) NOT NULL DEFAULT '0',
                        `created_time` bigint(20) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `name_UNIQUE` (`name`),
                        UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `transfer` (
                            `id` bigint(11) NOT NULL AUTO_INCREMENT,
                            `from_user` bigint(11) NOT NULL,
                            `to_user` bigint(11) NOT NULL,
                            `amount` decimal(10,0) NOT NULL,
                            `status` varchar(50) NOT NULL,
                            `created_time` bigint(11) NOT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `transaction` (
                               `id` bigint(11) NOT NULL AUTO_INCREMENT,
                               `user_id` bigint(11) DEFAULT NULL,
                               `tx_id` bigint(11) DEFAULT NULL,
                               `tx_type` varchar(50) DEFAULT NULL,
                               `amount` decimal(10,0) DEFAULT NULL,
                               `created_time` bigint(11) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;