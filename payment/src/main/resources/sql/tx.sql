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
