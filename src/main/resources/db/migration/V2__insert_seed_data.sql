-- ==============================
-- Очистка таблиц (для повторного запуска)
-- ==============================

TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_statuses RESTART IDENTITY CASCADE;
TRUNCATE TABLE customers RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;

-- ==============================
-- Заполнение таблицы products
-- ==============================

INSERT INTO products (description, price, quantity, category)
VALUES
('Ноутбук Lenovo ThinkPad', 85000.00, 5, 'Electronics'),
('Холодильник LG', 65000.00, 3, 'Appliances'),
('Смартфон Samsung Galaxy', 55000.00, 10, 'Electronics'),
('Микроволновка Panasonic', 15000.00, 7, 'Appliances'),
('Кроссовки Nike Air', 12000.00, 20, 'Shoes'),
('Стол письменный', 12000.00, 8, 'Furniture'),
('Наушники Sony', 8000.00, 12, 'Electronics'),
('Стул офисный', 5000.00, 15, 'Furniture'),
('Футболка Adidas', 2500.00, 30, 'Clothes'),
('Книга "Java для начинающих"', 1500.00, 50, 'Books');

-- ==============================
-- Заполнение таблицы customers
-- ==============================

INSERT INTO customers (name, phone, email)
VALUES
('Иван Петров', '8999000001', 'ivan@example.ru'),
('Мария Смирнова', '8999000002', 'maria@example.ru'),
('Сергей Иванов', '8999000003', 'sergey@example.ru'),
('Анна Кузнецова', '8999000004', 'anna@example.ru'),
('Дмитрий Соколов', '8999000005', 'dmitry@example.ru'),
('Елена Попова', '8999000006', 'elena@example.ru'),
('Павел Морозов', '8999000007', 'pavel@example.ru'),
('Ольга Орлова', '8999000008', 'olga@example.ru'),
('Алексей Новиков', '8999000009', 'alexey@example.ru'),
('Виктория Павлова', '8999000010', 'victoria@example.ru');

-- ==============================
-- Заполнение таблицы order_statuses
-- ==============================

INSERT INTO order_statuses (name)
VALUES
('Новый'),
('Оплачен'),
('Отправлен'),
('Отменён'),
('Возврат'),
('В обработке'),
('Доставлен'),
('Ожидает оплаты'),
('Ожидает отправки'),
('Приостановлен');

-- ==============================
-- Заполнение таблицы orders
-- ==============================

INSERT INTO orders (product_id, customer_id, order_date, status_id)
VALUES
-- Доставлен (45)
(9, 1, NOW() - INTERVAL '60 days', 7),
(8, 2, NOW() - INTERVAL '59 days', 7),
(6, 3, NOW() - INTERVAL '58 days', 7),
(7, 4, NOW() - INTERVAL '57 days', 7),
(3, 5, NOW() - INTERVAL '56 days', 7),
(3, 6, NOW() - INTERVAL '55 days', 7),
(10, 1, NOW() - INTERVAL '54 days', 7),
(9, 2, NOW() - INTERVAL '53 days', 7),
(8, 3, NOW() - INTERVAL '52 days', 7),
(6, 4, NOW() - INTERVAL '51 days', 7),
(7, 5, NOW() - INTERVAL '50 days', 7),
(3, 6, NOW() - INTERVAL '49 days', 7),
(10, 1, NOW() - INTERVAL '48 days', 7),
(9, 2, NOW() - INTERVAL '47 days', 7),
(8, 3, NOW() - INTERVAL '46 days', 7),
(6, 4, NOW() - INTERVAL '45 days', 7),
(7, 5, NOW() - INTERVAL '44 days', 7),
(3, 6, NOW() - INTERVAL '43 days', 7),
(10, 1, NOW() - INTERVAL '42 days', 7),
(9, 2, NOW() - INTERVAL '41 days', 7),
(8, 3, NOW() - INTERVAL '40 days', 7),
(6, 4, NOW() - INTERVAL '39 days', 7),
(7, 5, NOW() - INTERVAL '38 days', 7),
(3, 6, NOW() - INTERVAL '37 days', 7),
(10, 1, NOW() - INTERVAL '36 days', 7),
(9, 2, NOW() - INTERVAL '35 days', 7),
(8, 3, NOW() - INTERVAL '34 days', 7),
(6, 4, NOW() - INTERVAL '33 days', 7),
(7, 5, NOW() - INTERVAL '32 days', 7),
(3, 6, NOW() - INTERVAL '31 days', 7),
(10, 1, NOW() - INTERVAL '30 days', 7),
(9, 2, NOW() - INTERVAL '29 days', 7),
(8, 3, NOW() - INTERVAL '28 days', 7),
(6, 4, NOW() - INTERVAL '27 days', 7),
(7, 5, NOW() - INTERVAL '26 days', 7),
(3, 6, NOW() - INTERVAL '25 days', 7),
(10, 1, NOW() - INTERVAL '24 days', 7),
(9, 2, NOW() - INTERVAL '23 days', 7),
(8, 3, NOW() - INTERVAL '22 days', 7),
(6, 4, NOW() - INTERVAL '21 days', 7),
(7, 5, NOW() - INTERVAL '20 days', 7),
(3, 6, NOW() - INTERVAL '19 days', 7),
-- Ожидает оплаты (15)
(9, 1, NOW() - INTERVAL '18 days', 8),
(8, 2, NOW() - INTERVAL '17 days', 8),
(6, 3, NOW() - INTERVAL '16 days', 8),
(7, 4, NOW() - INTERVAL '15 days', 8),
(3, 5, NOW() - INTERVAL '14 days', 8),
(10, 1, NOW() - INTERVAL '13 days', 8),
(9, 2, NOW() - INTERVAL '12 days', 8),
(8, 3, NOW() - INTERVAL '11 days', 8),
(6, 4, NOW() - INTERVAL '10 days', 8),
(7, 5, NOW() - INTERVAL '9 days', 8),
(3, 6, NOW() - INTERVAL '8 days', 8),
(10, 1, NOW() - INTERVAL '7 days', 8),
(9, 2, NOW() - INTERVAL '6 days', 8),
(8, 3, NOW() - INTERVAL '5 days', 8),
(6, 4, NOW() - INTERVAL '4 days', 8),
-- Остальные статусы
-- Новый (5)
(6, 1, NOW() - INTERVAL '3 days', 1),
(9, 2, NOW() - INTERVAL '3 days', 1),
(8, 3, NOW() - INTERVAL '3 days', 1),
(7, 4, NOW() - INTERVAL '3 days', 1),
(3, 5, NOW() - INTERVAL '3 days', 1),
-- Оплачен (6)
(2, 1, NOW() - INTERVAL '2 days', 2),
(6, 2, NOW() - INTERVAL '2 days', 2),
(3, 3, NOW() - INTERVAL '2 days', 2),
(8, 4, NOW() - INTERVAL '2 days', 2),
(9, 5, NOW() - INTERVAL '2 days', 2),
(10, 1, NOW() - INTERVAL '2 days', 2),
-- Отправлен (12)
(3, 1, NOW() - INTERVAL '1 days', 3),
(6, 2, NOW() - INTERVAL '1 days', 3),
(7, 3, NOW() - INTERVAL '1 days', 3),
(8, 4, NOW() - INTERVAL '1 days', 3),
(9, 5, NOW() - INTERVAL '1 days', 3),
(10, 1, NOW() - INTERVAL '1 days', 3),
(3, 2, NOW() - INTERVAL '1 days', 3),
(6, 3, NOW() - INTERVAL '1 days', 3),
(7, 4, NOW() - INTERVAL '1 days', 3),
(8, 5, NOW() - INTERVAL '1 days', 3),
(9, 1, NOW() - INTERVAL '1 days', 3),
(10, 2, NOW() - INTERVAL '1 days', 3),
-- Отменён (2)
(3, 1, NOW() - INTERVAL '1 days', 4),
(6, 2, NOW() - INTERVAL '1 days', 4),
-- Возврат (2)
(7, 3, NOW() - INTERVAL '1 days', 5),
(8, 4, NOW() - INTERVAL '1 days', 5),
-- В обработке (5)
(9, 5, NOW() - INTERVAL '1 days', 6),
(10, 1, NOW() - INTERVAL '1 days', 6),
(3, 2, NOW() - INTERVAL '1 days', 6),
(6, 3, NOW() - INTERVAL '1 days', 6),
(7, 4, NOW() - INTERVAL '1 days', 6),
-- Ожидает отправки (7)
(8, 5, NOW() - INTERVAL '1 days', 9),
(9, 1, NOW() - INTERVAL '1 days', 9),
(10, 2, NOW() - INTERVAL '1 days', 9),
(3, 3, NOW() - INTERVAL '1 days', 9),
(6, 4, NOW() - INTERVAL '1 days', 9),
(7, 5, NOW() - INTERVAL '1 days', 9),
(8, 1, NOW() - INTERVAL '1 days', 9),
-- Приостановлен (1)
(9, 2, NOW() - INTERVAL '1 days', 10);