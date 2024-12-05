## DATABASE 생성확인
DROP DATABASE IF EXISTS kylemall;
CREATE DATABASE IF NOT EXISTS kylemall;
USE kylemall;

######## 회원 ########
DROP TABLE IF EXISTS member;
CREATE TABLE IF NOT EXISTS member (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    pass VARCHAR(100) NOT NULL,
    email VARCHAR(30) NOT NULL,
    mobile VARCHAR(13) NOT NULL,
    zipcode VARCHAR(5) NOT NULL,
    address1 VARCHAR(80) NOT NULL,
    address2 VARCHAR(60) NOT NULL,
    email_get TINYINT(1) NOT NULL,
    reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nickname VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM member;

######## 카테고리 ########
DROP TABLE IF EXISTS category;
CREATE TABLE IF NOT EXISTS category (
    category_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######## 상품 ########
DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product (
    product_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    product_description TEXT NOT NULL,
    product_price INTEGER NOT NULL,
    stock_quantity INTEGER NOT NULL,
    image_url VARCHAR(255) DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    category_no INTEGER NOT NULL,
    sale_ok TINYINT(1) NOT NULL,
    sale_price INTEGER NULL,
    CONSTRAINT category_no_fk FOREIGN KEY (category_no) REFERENCES category(category_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM product;

######## 장바구니 ########
DROP TABLE IF EXISTS shoppingcart;
CREATE TABLE IF NOT EXISTS shoppingcart (
    cart_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    quantity INTEGER NOT NULL,added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    member_id VARCHAR(20) NOT NULL,
    product_no INTEGER NOT NULL,
    CONSTRAINT member_id_fk FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT product_no_fk FOREIGN KEY (product_no) REFERENCES product(product_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######## 주문 ########
DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders (
	order_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_id VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    order_status VARCHAR(10) DEFAULT '주문 완료',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT orders_member_id_fk FOREIGN KEY (member_id) REFERENCES member(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######## 주문상세 ########
CREATE TABLE IF NOT EXISTS orderdetail (
	detail_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    detail_quantity INTEGER NOT NULL,
    order_id INTEGER NOT NULL,
    product_no INTEGER NOT NULL,
    CONSTRAINT detail_order_fk FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT detail_product_fk FOREIGN KEY (product_no) REFERENCES product(product_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM orderdetail;

######## 결제 ########
CREATE TABLE IF NOT EXISTS payment (
	payment_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    order_id INTEGER NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(20) DEFAULT '결제 대기',
    amount DECIMAL(10, 2) NOT NULL,
    paid_at TIMESTAMP NULL, # 결제가 완료된 시점을 기록하는 필드
    payment_gateway_id VARCHAR(100) NULL, # 결제 게이트웨이의 ID를 저장하는 필드
    approval_number VARCHAR(50) NULL, # 결제 승인 번호를 저장하는 필드
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######## 배송 정보 ########
CREATE TABLE IF NOT EXISTS shipping (
    shipping_id INTEGER AUTO_INCREMENT PRIMARY KEY, -- 배송 ID, 자동 증가
    order_id INTEGER NOT NULL,                      -- 주문 ID (Foreign Key)
    recipient_name VARCHAR(100) NOT NULL,      -- 수령인 이름
    address VARCHAR(255) NOT NULL,              -- 배송 주소
    phone_number VARCHAR(15) NOT NULL,          -- 수령인 전화번호
    delivery_status ENUM('배송 준비 중', '배송 중', '배송 완료', '배송 취소') DEFAULT '배송 준비 중', -- 배송 상태
    tracking_number VARCHAR(50),                 -- 운송장 번호
    shipping_date DATETIME,                      -- 배송 시작일
    estimated_arrival DATETIME,                  -- 예상 도착일
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정일
    FOREIGN KEY (order_id) REFERENCES orders(order_id) -- order_id는 orders 테이블의 외래 키
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######## 데이터 삽입 ########
-- 회원
INSERT INTO member VALUES('rlaxoals97', '김태민', '$2a$10$n6QovfeGX1u1EMXfVTGP3u8opMRfnJ4fVxjNReJkBcukOmZLjDTgq', 'rlaxoals97@naver.com', '010-5578-5037', '08759', '서울 관악구 신림동7길 28 (신림동)', '402호', 1, '2024-11-04 18:26:08', 'Kyle');

-- 카테고리
INSERT INTO category VALUES(1, '아우터');
INSERT INTO category VALUES(2, '상의');
INSERT INTO category VALUES(3, '하의');

-- 상품
-- 상품 데이터 삽입 (30개)
INSERT INTO product (product_name, product_description, product_price, stock_quantity, image_url, created_at, updated_at, category_no, sale_ok, sale_price) VALUES
('빅사이즈 후드티', '따뜻하고 편안한 빅사이즈 후드티입니다.', 35000, 20, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 30000),
('라운드넥 니트', '부드러운 라운드넥 니트로 캐주얼한 스타일.', 25000, 15, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 20000),
('데님 청바지', '신축성 좋은 데님 소재 청바지.', 40000, 30, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('롱패딩', '한겨울을 따뜻하게 보낼 롱패딩입니다.', 120000, 10, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 100000),
('체크 셔츠', '스타일리시한 체크 패턴 셔츠입니다.', 30000, 25, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('슬림핏 슬랙스', '직장인 필수템, 슬림핏 슬랙스.', 45000, 18, NULL, CURRENT_TIMESTAMP, NULL, 3, 1, 40000),
('기모 맨투맨', '기모 소재로 보온성을 높인 맨투맨입니다.', 28000, 40, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 25000),
('울 코트', '세련된 디자인의 울 코트.', 85000, 8, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 80000),
('하프팬츠', '여름철 필수템 하프팬츠.', 20000, 50, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('패딩 조끼', '가볍고 따뜻한 패딩 조끼입니다.', 60000, 12, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 55000),
('헨리넥 티셔츠', '캐주얼한 스타일의 헨리넥 티셔츠.', 27000, 35, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('와이드 팬츠', '편안함과 스타일을 동시에 제공하는 와이드 팬츠.', 42000, 20, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('경량 패딩', '가볍고 따뜻한 경량 패딩.', 75000, 15, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 70000),
('프린트 반팔 티', '개성 있는 프린트 디자인 반팔 티.', 22000, 60, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('트레이닝 팬츠', '운동하기 좋은 트레이닝 팬츠.', 32000, 25, NULL, CURRENT_TIMESTAMP, NULL, 3, 1, 30000),
('양털 집업', '양털 소재로 보온성을 극대화한 집업.', 50000, 10, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 45000),
('카라 티셔츠', '깔끔한 디자인의 카라 티셔츠.', 26000, 30, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 23000),
('린넨 팬츠', '시원하고 편안한 린넨 팬츠.', 35000, 40, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('후리스 자켓', '가벼운 후리스 소재 자켓입니다.', 48000, 12, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 45000),
('스트라이프 티', '심플한 스트라이프 디자인 티셔츠.', 24000, 50, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('카고 팬츠', '다용도 주머니가 특징인 카고 팬츠.', 37000, 18, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('가죽 자켓', '고급스러운 가죽 소재의 자켓.', 130000, 5, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 125000),
('V넥 스웨터', '부드러운 V넥 디자인의 스웨터.', 27000, 28, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 25000),
('조거 팬츠', '활동성을 강조한 조거 팬츠.', 39000, 22, NULL, CURRENT_TIMESTAMP, NULL, 3, 1, 35000),
('트렌치 코트', '클래식한 디자인의 트렌치 코트.', 95000, 6, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 90000),
('포켓 셔츠', '실용적인 포켓이 있는 셔츠.', 31000, 40, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('밴딩 팬츠', '편안한 착용감을 제공하는 밴딩 팬츠.', 34000, 30, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('다운 자켓', '보온성을 높인 다운 자켓.', 140000, 7, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 135000),
('스웨트 셔츠', '기본 디자인의 스웨트 셔츠.', 26000, 60, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 24000),
('배기 팬츠', '스타일리시한 배기 팬츠.', 38000, 24, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL);


-- 장바구니
INSERT INTO shoppingcart VALUES(1, 1, CURRENT_TIMESTAMP, 'rlaxoals97', 1);

SELECT 
        sc.cart_id AS cartId,             
        sc.quantity AS quantity,         
        sc.added_at AS addedAt,          
        sc.member_id AS memberId,           
        p.product_no AS productNo,        
        p.product_name AS productName,   
        p.product_price AS productPrice,    
        p.image_url AS imageUrl            
    FROM shoppingcart sc
    JOIN product p ON sc.product_no = p.product_no;