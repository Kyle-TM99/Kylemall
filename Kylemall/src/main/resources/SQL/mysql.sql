## DATABASE 생성확인
DROP DATABASE IF EXISTS kylemall;
CREATE database if not exists kylemall;
use kylemall;

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
CONSTRAINT category_no_fk FOREIGN KEY (category_no) REFERENCES category(category_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO product VALUES(1,'김건우','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);
INSERT INTO product VALUES(2,'휴대폰','그냥 쥰내 평범한 휴대폰', 4500000, 12, NULL, sysdate(), NULL, 2);
INSERT INTO product VALUES(3,'컴퓨터','롤도 겨우 돌아가는데 애플이 만들어서 쥰내 비싸기만 컴퓨터', 1200000000, 63, NULL, sysdate(), NULL, 2);
INSERT INTO product VALUES(4,'ㅋㅋㅋ','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);
INSERT INTO product VALUES(5,'김건우','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);
INSERT INTO product VALUES(6,'김건우','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);
INSERT INTO product VALUES(7,'김건우','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);
INSERT INTO product VALUES(8,'김건우','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);
INSERT INTO product VALUES(19,'김건우','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);
INSERT INTO product VALUES(10,'김건우','레전드가 될 뻔한 남자', 4500, 1, NULL, sysdate(), NULL, 1);

SELECT * FROM product;

DROP TABLE IF EXISTS category;
CREATE TABLE IF NOT EXISTS category (
    category_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(10) NOT NULL,
    parent_no INTEGER NULL,
    CONSTRAINT parent_no_fk FOREIGN KEY (parent_no) REFERENCES category(category_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO category VALUES(1, '인간', NULL);
INSERT INTO category VALUES(2, '전자기기', NULL);

SELECT * FROM category;
