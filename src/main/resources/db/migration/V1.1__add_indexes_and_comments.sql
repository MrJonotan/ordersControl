-- Добавление индексов
CREATE INDEX IF NOT EXISTS idx_order_product_id
ON orders (product_id);

CREATE INDEX IF NOT EXISTS idx_order_customer_id
ON orders (customer_id);

-- Индекс по дате заказа
CREATE INDEX IF NOT EXISTS idx_order_date
ON orders (order_date);

-- Добавление комментариев
-- Таблицам
COMMENT ON TABLE products IS 'Товары, доступные для заказа';
COMMENT ON TABLE customers IS 'Клиенты, делающие заказы';
COMMENT ON TABLE orders IS 'Заказы клиентов';
COMMENT ON TABLE order_statuses IS 'Справочник возможных статусов заказов';

-- Полям
COMMENT ON COLUMN products.id IS 'Уникальный идентификатор товара';
COMMENT ON COLUMN products.description IS 'Описание товара';
COMMENT ON COLUMN products.price IS 'Цена товара, не может быть отрицательной';
COMMENT ON COLUMN products.quantity IS 'Количество на складе, >= 0';
COMMENT ON COLUMN orders.product_id IS 'Ссылка на товар';
COMMENT ON COLUMN orders.customer_id IS 'Ссылка на клиента';
COMMENT ON COLUMN orders.status_id IS 'Ссылка на справочник статусов';
COMMENT ON COLUMN order_statuses.name IS 'Название статуса заказа';