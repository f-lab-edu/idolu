CREATE TABLE `product` (
    `product_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '상품 id',
    `product_identifier` varchar(50) UNIQUE COMMENT '상품 외부 id',
    `store_id` varchar(30) COMMENT '회원사 id',
    `stock` integer COMMENT '재고',
    `name` varchar(200) COMMENT '상품명',
    `product_status` varchar(50) COMMENT '상품 상태',
    `version` bigint,
    `apply_round_discount` boolean DEFAULT false COMMENT '기간별 할인 여부',
    `basic_price` decimal(10,4) COMMENT '기본 가격',
    `selling_price` decimal(10,4) COMMENT '할인 가격',
    `discount_rate` int COMMENT '할인율',
    `discount_one_time` boolean COMMENT '일회성 할인 여부',
    `contract_period` int COMMENT '계약 기간',
    `contract_period_unit_code` varchar(30) COMMENT '계약 기간 단위',
    `service_period` int COMMENT '서비스 제공주기',
    `service_period_unit_code` varchar(30) COMMENT '서비스 제공주기 단위',
    `product_information` json COMMENT '상품 정보',
    `deleted` boolean DEFAULT false COMMENT '삭제 여부',
    `created_at` datetime COMMENT '생성 일시',
    `updated_at` datetime COMMENT '최종 수정 일시'
);

CREATE TABLE `product_discount` (
    `product_discount_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '연관상품 할인 id',
    `product_id` bigint COMMENT '연관상품 id',
    `discount_round` int COMMENT '적용 회차',
    `discount_value` decimal(10,4) COMMENT '할인 금액/할인율',
    `discount_code` varchar(30) COMMENT '할인 유형',
    `deleted` boolean DEFAULT false COMMENT '삭제 여부',
    `created_at` datetime COMMENT '생성 일시',
    `updated_at` datetime COMMENT '최종 수정 일시'
);

CREATE TABLE `product_image` (
    `product_image_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '상품 이미지 id',
    `product_id` bigint COMMENT '상품 id',
    `image_type` varchar(30) COMMENT '이미지 유형',
    `url` text COMMENT '파일 url',
    `sort_number` int COMMENT '정렬 순서',
    `deleted` boolean DEFAULT false COMMENT '삭제 여부',
    `created_at` datetime COMMENT '생성 일시',
    `updated_at` datetime COMMENT '최종 수정 일시'
);

CREATE TABLE `category` (
    `category_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '카테고리 id',
    `category_code` varchar(30) COMMENT '카테고리 코드',
    `category_name` varchar(100) COMMENT '카테고리명',
    `deleted` boolean DEFAULT false COMMENT '삭제 여부',
    `created_at` datetime COMMENT '생성 일시',
    `updated_at` datetime COMMENT '최종 수정 일시'
);

CREATE TABLE `product_category` (
    `product_category_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '상품 카테고리 id',
    `product_id` bigint COMMENT '상품 id',
    `category_id` bigint COMMENT '카테고리 id',
    `deleted` boolean DEFAULT false COMMENT '삭제 여부',
    `created_at` datetime COMMENT '생성 일시',
    `updated_at` datetime COMMENT '최종 수정 일시'
);

CREATE TABLE `store` (
    `store_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '회원사 id',
    `store_code` varchar(100) COMMENT '구별가능한 서비스 키',
    `store_name` varchar(100) COMMENT '회원사 명',
    `representation_name` varchar(100) COMMENT '대표자 명',
    `base_address` varchar(500) COMMENT '기본 주소',
    `detail_address` varchar(500) COMMENT '상세 주소',
    `profile_image_url` text COMMENT '프로필 이미지',
    `deleted` boolean DEFAULT false COMMENT '삭제 여부',
    `created_at` datetime COMMENT '생성 일시',
    `updated_at` datetime COMMENT '최종 수정 일시'
);

CREATE TABLE `inventory_update_event` (
    `inventory_update_event_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '재고 요청 관리 id',
    `product_identifier` varchar(50) COMMENT '상품 외부 id',
    `order_number` varchar(50) COMMENT '주문 외부 id',
    `event_type` varchar(30) COMMENT '이벤트 타입',
    `event_status` varchar(30) COMMENT '이벤트 상태',
    `created_at` datetime COMMENT '생성 일시'
);
