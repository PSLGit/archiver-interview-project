CREATE DATABASE IF NOT EXISTS `archiver_db`;
USE `archiver_db`;

CREATE TABLE `file` (
    `id` int UNSIGNED NOT NULL,
    `name` varchar(128) NOT NULL
);

CREATE TABLE `file_history` (
   `id` int UNSIGNED NOT NULL,
   `status` varchar(128) NOT NULL,
   `file_id` int UNSIGNED NOT NULL
);

ALTER TABLE `file`
    MODIFY `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);
ALTER TABLE `file_history`
    MODIFY `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);