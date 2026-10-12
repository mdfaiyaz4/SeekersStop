CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(255) NOT NULL,
  `role` ENUM('JOB_SEEKER','RECRUITER') NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `UK_user_username` UNIQUE (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `company` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contact_info` VARCHAR(255) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `location` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `website` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `job_seeker` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contact` VARCHAR(255) DEFAULT NULL,
  `cv` VARCHAR(255) DEFAULT NULL,
  `experience` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  `skill` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `UK_job_seeker_user_id` UNIQUE (`user_id`),
  CONSTRAINT `FKt6no6vfq2vtvqbwlqyik9hyef`
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruiter` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contact_info` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `company_id` BIGINT DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `UK_recruiter_user_id` UNIQUE (`user_id`),
  KEY `FKe5tll0cw7cnohojpxb8qjcr5y` (`company_id`),
  CONSTRAINT `FK63kq3uyt2p3i32pjo1nfin63a`
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKe5tll0cw7cnohojpxb8qjcr5y`
    FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `job` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `deadline` DATE DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `experience` VARCHAR(255) DEFAULT NULL,
  `location` VARCHAR(255) DEFAULT NULL,
  `qualification` VARCHAR(255) DEFAULT NULL,
  `salary` DOUBLE DEFAULT NULL,
  `title` VARCHAR(255) DEFAULT NULL,
  `company_id` BIGINT DEFAULT NULL,
  `recruiter_id` BIGINT DEFAULT NULL,
  `active` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5q04favsasq8y70bsei7wv8fc` (`company_id`),
  KEY `FKo8ddh7vevgs1i7fn6u0p2g5b2` (`recruiter_id`),
  CONSTRAINT `FK5q04favsasq8y70bsei7wv8fc`
    FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKo8ddh7vevgs1i7fn6u0p2g5b2`
    FOREIGN KEY (`recruiter_id`) REFERENCES `recruiter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `application` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `applied_at` DATETIME(6) DEFAULT NULL,
  `status` ENUM('ACCEPTED','PENDING','REJECTED','SHORTLISTED') DEFAULT NULL,
  `job_id` BIGINT NOT NULL,
  `job_seeker_id` BIGINT NOT NULL,
  `submitted_cv_path` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKls6sryk64ga8o5t4bym8qu3vm` (`job_id`),
  KEY `FKg7bshxgetwt3lb5whvku5sjay` (`job_seeker_id`),
  CONSTRAINT `UK_application_job_job_seeker`
    UNIQUE (`job_id`, `job_seeker_id`),
  CONSTRAINT `FKg7bshxgetwt3lb5whvku5sjay`
    FOREIGN KEY (`job_seeker_id`) REFERENCES `job_seeker` (`id`),
  CONSTRAINT `FKls6sryk64ga8o5t4bym8qu3vm`
    FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
