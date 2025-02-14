CREATE TABLE `product`
(
    `product_id`  bigint PRIMARY KEY AUTO_INCREMENT,
    `stock`       integer,
    `name`        varchar(255),
    `image_url`   varchar(255),
    `description` varchar(255),
    `price`       decimal,
    `status`      varchar(255),
    `version`     bigint,
    `is_deleted`  boolean,
    `created_at`  datetime,
    `updated_at`  datetime
);

CREATE TABLE `category`
(
    `category_id`   bigint PRIMARY KEY AUTO_INCREMENT,
    `category_code` varchar(255),
    `is_deleted`    boolean,
    `created_at`    datetime,
    `updated_at`    datetime
);

CREATE TABLE `product_category`
(
    `product_category_id` bigint PRIMARY KEY AUTO_INCREMENT,
    `product_id`          bigint,
    `category_id`         bigint,
    `is_deleted`          boolean,
    `created_at`          datetime,
    `updated_at`          datetime
);

