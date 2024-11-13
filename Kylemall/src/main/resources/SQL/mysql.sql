## DATABASE 생성확인
DROP DATABASE IF EXISTS kylemall;
CREATE database if not exists kylemall;
use kylemall;

######## 회원 ########
DROP TABLE IF EXISTS member;
CREATE TABLE IF NOT EXISTS member(
id VARCHAR(20) PRIMARY KEY,
name VARCHAR(10) NOT NULL,
pass VARCHAR(100) NOT NULL,
email VARCHAR(30) NOT NULL,
mobile VARCHAR(13) NOT NULL,
zipcode  VARCHAR(5) NOT NULL,
address1  VARCHAR(80) NOT NULL,
address2  VARCHAR(60) NOT NULL,
email_get VARCHAR(1) NOT NULL,
reg_date  TIMESTAMP NOT NULL,
nickname VARCHAR(20) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT * FROM member;
INSERT INTO member VALUES('rlaxoals97', '김태민', '$2a$10$n6QovfeGX1u1EMXfVTGP3u8opMRfnJ4fVxjNReJkBcukOmZLjDTgq', 'rlaxoals97@naver.com', '010-5578-5037', '08759', '서울 관악구 신림동7길 28 (신림동)', '402호', '1', '2024-11-04 18:26:08', 'Kyle');

######## 상품 ########
DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product(
product_no INTEGER AUTO_INCREMENT PRIMARY KEY,
product_name VARCHAR(50) NOT NULL,
product_description BLOB NOT NULL,
product_price INTEGER NOT NULL,
stock_quantity INTEGER NOT NULL,
image_url VARCHAR(255) NULL,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NULL,
category_no INTEGER NOT NULL,
sale_ok VARCHAR(1) NOT NULL,
sale_price INTEGER NULL,
CONSTRAINT category_no_fk FOREIGN KEY (category_no) REFERENCES category(category_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO product VALUES(1,'후드집업','오버핏 느낌의 후드집업입니다.', 45000, 52, NULL, sysdate(), NULL, 1, '1', 40000);
INSERT INTO product VALUES(2,'줄무늬반팔','편하게 입기좋은 반팔티입니다.', 25000, 12, NULL, sysdate(), NULL, 2, '0', NULL);
INSERT INTO product VALUES(3,'슬랙스','단정하 느낌을 주는 긴바지입니다.', 41000, 27, NULL, sysdate(), NULL, 3, '0', NULL);

SELECT * FROM product;

######## 카테고리 ########
DROP TABLE IF EXISTS category;
CREATE TABLE IF NOT EXISTS category (
    category_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO category VALUES(1, '아우터');
INSERT INTO category VALUES(2, '상의');
INSERT INTO category VALUES(3, '하의');

SELECT * FROM category;
