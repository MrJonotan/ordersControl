-- Запросы на получение
-- 1) Получение списка товаров доступных для заказа (готво)
select *
from products p
where p.deleted_at is null;

-- 2) Получение всех заказов клиента
select c."name" as customerName, p.description as productName, p.price, o.order_date, os."name" as statusName
from orders o
left join products p on o.product_id = p.id
left join customers c on o.customer_id = c.id
left join order_statuses os on o.status_id = os.id
where o.customer_id = (select c.id from customers c where c."name" like 'Иван%')

-- 3) Получение статистики клиента
select count(o.*), sum(p.price) as totalPrice
from orders o
left join products p on o.product_id = p.id
where o.customer_id = (select c.id from customers c where c."name" like 'Иван%')

-- 4) Топ-3 самых популярных товара
select p.description, count(o.*)
from orders o
left join products p on o.product_id = p.id
group by o.product_id, p.description
order by 2 desc
limit 3

-- 5) Топ 5 покупателей месяца
select c."name", sum(p.price) as sumPrice
from orders o
left join products p on o.product_id = p.id
left join customers c on o.customer_id = c.id
left join order_statuses os on o.status_id = os.id
where o.order_date > now() - INTERVAL '30 days'
group by o.customer_id, c."name"
order by sumPrice desc
limit 5

-- Запросы на обновление
--  1) Обновить статус заказа
update orders o
set status_id = 4
where o.id = 34;
select * from orders o where o.id = 34;

--  2) Обновление количества на складе при покупке.
update products p
set quantity = p.quantity -1
where p.id = 5;
-- проверочный запрос
select * from products p where p.id = 5

-- 3) Обновление телефона клиента
update customers c
set phone = '9090324009'
where c.id = 7;
-- проверочный запрос
select * from customers c where c.id = 7

-- Запросы на удаление
-- 1) Удаление продукта (закончился на складе)
update products p
set deleted_at = now()
where p.id = 4;
-- проверочный запрос
select * from products p where p.deleted_at is null

-- 2) Удаление клиента
update customers c
set deleted_at = now();
where c.id = 7;
-- проверочный запрос
select * from customers c where с.deleted_at is null

-- 3) Удаление заказа
delete orders o
where o.id = 97;
-- проверочный запрос
select * from orders