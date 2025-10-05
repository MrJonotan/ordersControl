CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    description VARCHAR(300),
    price NUMERIC(10, 2) CHECK (price > 0),
    quantity INTEGER CHECK (quantity > 0),
    category VARCHAR(50),
    deleted_at TIMESTAMP NULL
);

-- Таблица клиенты
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    email VARCHAR(50) NOT NULL,
    deleted_at TIMESTAMP NULL
);

-- Таблица статусы заказов
CREATE TABLE IF NOT EXISTS order_statuses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    deleted_at TIMESTAMP NULL
);

-- Таблица заказы
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(id),
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    order_date TIMESTAMP NOT NULL,
    status_id INTEGER NOT NULL REFERENCES order_statuses(id)
);
