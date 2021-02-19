DROP DATABASE IF EXISTS ciklum_app;

CREATE DATABASE ciklum_app;
USE ciklum_app;

CREATE TABLE `users`(
id int AUTO_INCREMENT,
login varchar(45) NOT NULL UNIQUE,
password varchar(255) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE `orders`(
id int AUTO_INCREMENT,
user_id int NOT NULL,
status varchar(45) NOT NULL,
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE `products`(
id int AUTO_INCREMENT,
name varchar(45) NOT NULL UNIQUE,
price int NOT NULL DEFAULT 0,
products_status enum('out_of_stock','in_stock','running_low') DEFAULT 'in_stock',
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(id)
);

CREATE TABLE `order_items`(
order_id int NOT NULL,
product_id int NOT NULL,
quantity int DEFAULT 1,
FOREIGN KEY(order_id) REFERENCES orders(id)
ON DELETE CASCADE,
FOREIGN KEY(product_id) REFERENCES products(id)
ON DELETE CASCADE
);

ALTER DATABASE ciklum_app CHARACTER SET utf8 COLLATE utf8_general_ci;

INSERT INTO users(login,password) VALUES('adminn','Admin1@@');

INSERT INTO products(name,price,products_status)
VALUES('phone', 10, 'out_of_stock'),
      ('tv', 100, 'in_stock'),
      ('plane', 10000, 'running_low');

INSERT INTO orders(user_id,status)
VALUES(1,'created');

INSERT INTO order_items(order_id,product_id,quantity)
VALUES(1,2,5),(1,3,6);