DROP TABLE IF EXISTS customer_order;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS product;

CREATE TABLE customer (
                          id int AUTO_INCREMENT primary key,
                          name VARCHAR(100),
                          email VARCHAR(100)
);

CREATE TABLE product (
                         id int AUTO_INCREMENT primary key,
                         description VARCHAR(100),
                         price int
);

-- Orders
-- MySQL has no native UUID column type; use CHAR(36) (or BINARY(16) if you prefer).
-- Defaulting to UUID() directly isn’t allowed in column DEFAULT, so we use a trigger below.
CREATE TABLE `customer_order` (
                                  `order_id` CHAR(36) NOT NULL PRIMARY KEY,
                                  `customer_id` INT NOT NULL,
                                  `product_id` INT NOT NULL,
                                  `amount` INT,
                                  `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT `fk_customer_order_customer`
                                      FOREIGN KEY (`customer_id`) REFERENCES `customer`(`id`) ON DELETE CASCADE,
                                  CONSTRAINT `fk_customer_order_product`
                                      FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Auto-generate UUIDs if order_id not provided
DELIMITER //
CREATE TRIGGER `bi_customer_order_uuid`
    BEFORE INSERT ON `customer_order`
    FOR EACH ROW
BEGIN
    IF NEW.`order_id` IS NULL OR NEW.`order_id` = '' THEN
    SET NEW.`order_id` = UUID();
END IF;
END//
DELIMITER ;

INSERT INTO `customer`(`name`, `email`) VALUES
                                            ('sam', 'sam@gmail.com'),
                                            ('mike', 'mike@gmail.com'),
                                            ('jake', 'jake@gmail.com'),
                                            ('emily', 'emily@example.com'),
                                            ('sophia', 'sophia@example.com'),
                                            ('liam', 'liam@example.com'),
                                            ('olivia', 'olivia@example.com'),
                                            ('noah', 'noah@example.com'),
                                            ('ava', 'ava@example.com'),
                                            ('ethan', 'ethan@example.com');

-- Seed products
INSERT INTO `product`(`description`, `price`) VALUES
                                                  ('iphone 20', 1000),
                                                  ('iphone 18', 750),
                                                  ('ipad', 800),
                                                  ('mac pro', 3000),
                                                  ('apple watch', 400),
                                                  ('macbook air', 1200),
                                                  ('airpods pro', 250),
                                                  ('imac', 2000),
                                                  ('apple tv', 200),
                                                  ('homepod', 300);

-- Order 1: sam buys an iphone 20 & iphone 18
INSERT INTO `customer_order` (`customer_id`, `product_id`, `amount`, `order_date`)
VALUES
    (1, 1, 950,  CURRENT_TIMESTAMP),
    (1, 2, 850,  CURRENT_TIMESTAMP);

-- Order 2: mike buys an iphone 20 and mac pro
INSERT INTO `customer_order` (`customer_id`, `product_id`, `amount`, `order_date`)
VALUES
    (2, 1, 975,   CURRENT_TIMESTAMP),
    (2, 4, 2999,  CURRENT_TIMESTAMP);

-- Order 3: jake buys an iphone 18 & ipad  (fixed second line to product_id = 3)
INSERT INTO `customer_order` (`customer_id`, `product_id`, `amount`, `order_date`)
VALUES
    (3, 2, 750,  CURRENT_TIMESTAMP),
    (3, 3, 775,  CURRENT_TIMESTAMP);