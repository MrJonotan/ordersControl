package orderscontrol.repository.Impl;

import orderscontrol.model.Order;
import orderscontrol.repository.OrderRepository;
import orderscontrol.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

// полностью готово
public class OrderRepositoryImpl implements OrderRepository {
    // Основа запроса на получение заказов
    private final String selectQuery = "SELECT o.id, c.\"name\" as customerName, p.description as productName, o.order_date as orderDate, os.\"name\" as statusName " +
            "FROM orders o " +
            "LEFT JOIN products p ON o.product_id = p.id " +
            "LEFT JOIN customers c ON o.customer_id = c.id " +
            "LEFT JOIN order_statuses os ON o.status_id = os.id ";

    // Получение списка всех заказов клиента
    @Override
    public List<Order> findByCustomer(String customerName) {
        List<Order> list = new ArrayList<>();
        String sql =  selectQuery + " WHERE o.customer_id = (SELECT c.id FROM customers c  WHERE c.name LIKE ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Получение списка заказов по названию продукта
    @Override
    public List<Order> findByProduct(String productName) {
        List<Order> list = new ArrayList<>();
        String sql = selectQuery + " WHERE o.product_id = (SELECT p.id FROM products p  WHERE p.description LIKE ?)";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка: при получении товаров", e);
        }
        return list;
    }

    // Получение заказов отфильтрованных по статусу
    @Override
    public List<Order> findByStatus(String statusName) {
        List<Order> list = new ArrayList<>();
        String sql = selectQuery + " WHERE o.product_id = (SELECT os.id FROM order_statuses os  WHERE os.name = ?)";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Получить заказы за конкретную дату
    @Override
    public List<Order> findByOrderDate(LocalDateTime orderDate){
        List<Order> list = new ArrayList<>();
        String sql = selectQuery + " WHERE o.order_date = ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(orderDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Поиск записи по id
    @Override
    public Optional<Order> findById(int id) {
        Order order = null;
        String sql = selectQuery + " WHERE o.id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            order = mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(order);
    }

    // Вставить запись
    @Override
    public Order create(Order entity) {
        Connection conn = null;
        String sql = "INSERT INTO orders (product_id, customer_id, order_date, status_id) VALUES (?, ?, ?, ?)";
        try {
            conn = DBConnection.getInstance().getConnection(); // установка соединения
            conn.setAutoCommit(false); // отключение автокоммита
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, entity.getProductId());
                ps.setInt(2, entity.getCustomerId());
                ps.setTimestamp(3,  Timestamp.valueOf(entity.getOrderDate()));
                ps.setInt(4, entity.getStatusId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int id = rs.getInt(1);
                            entity.setId(id); // присваиваем id созданного клиента объекту
                        }
                    }
                }
                conn.commit();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                System.out.println("Заказ от: " + entity.getOrderDate().format(formatter) + " успешно добавлен");
                return entity;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении заказа: " + e.getMessage());
            try{
                conn.rollback(); // откат транзакции
                System.out.println("Транзакция откатилась.");
            } catch (SQLException rollbackEx) {
                System.err.println("Ошибка при rollback: " + rollbackEx.getMessage());
            }
        }
        return entity;
    }

    public List<Order> findLastFiveOrders() {
        List<Order> list = new ArrayList<>();
        String sql = selectQuery + " ORDER BY o.order_date DESC LIMIT 5";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Вывести все записи
    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Удалить запись по id
    @Override
    public void deleteById(int id) {
        Connection conn = null;
        String sql = "DELETE FROM orders o WHERE o.id = ?";
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Заказ с id : " + id + " успешно удален. ");
                } else {
                    System.out.println("Заказ с id " + id + " не найден");
                }
                conn.commit();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении заказа: " + e.getMessage());
            try{
                conn.rollback(); // откат транзакции
                System.out.println("Транзакция откатилась.");
            } catch (SQLException rollbackEx) {
                System.err.println("Ошибка при rollback: " + rollbackEx.getMessage());
            }
        }
    }

    // Создание экземпляра класса из полученной строки
    private Order mapRow(ResultSet rs) throws SQLException {
        return new Order(rs.getInt("id"),
            rs.getString("customerName"),
            rs.getString("productName"),
            rs.getTimestamp("orderDate").toLocalDateTime(),
            rs.getString("statusName")
        );
    }
}

