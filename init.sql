USE `book_storage`;

CREATE TABLE `book` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `isbn` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
                        `title` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
                        `genre` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
                        `description` VARCHAR(1000) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
                        `author` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
                        `creation_status` ENUM('EXISTS','DELETED') NOT NULL DEFAULT 'EXISTS' COLLATE 'utf8mb4_0900_ai_ci',
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE INDEX `isbn` (`isbn`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `book_info` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                             `book_id` BIGINT NOT NULL,
                             `status` ENUM('AVAILABLE','BORROWED','DELETED') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
                             `borrowed_at` DATETIME NULL DEFAULT NULL,
                             `return_due` DATETIME NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `book_id` (`book_id`) USING BTREE,
                             CONSTRAINT `FK_book_info_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
    COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;
