package orderscontrol.repository.Impl;

import orderscontrol.model.OrderStatus;
import orderscontrol.repository.CrudRepository;
import orderscontrol.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Полностью готово
public class OrderStatusRepositoryImpl implements CrudRepository<OrderStatus> {
    // Вставка записи
    @Override
    public OrderStatus create(OrderStatus entity) {
        Connection conn = null;
        String sql = "INSERT INTO order_statuses (name) VALUES (?)";
        try {
            conn = DBConnection.getInstance().getConnection(); // установка соединения
            conn.setAutoCommit(false); // отключение автокоммита
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getStatusName());
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
                System.out.println("Статус: " + entity.getStatusName() + " успешно добавлен");
                return entity;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении товара: " + e.getMessage());
            try{
                conn.rollback(); // откат транзакции
                System.out.println("Транзакция откатилась.");
            } catch (SQLException rollbackEx) {
                System.err.println("Ошибка при rollback: " + rollbackEx.getMessage());
            }
        }
        return entity;
    }

    // Поиск статуса заказа из справочника по id
    @Override
    public Optional<OrderStatus> findById(int id) {
        OrderStatus orderStatus = null;
        String sql = "SELECT os.name as statusName FROM order_statuses os WHERE os.id = ? AND deleted_at IS NULL";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            orderStatus = mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(orderStatus);
    }

    public int findByName(String name) {
        int orderStatusId = 0;
        String sql = "SELECT os.id FROM order_statuses os WHERE os.name = ? AND deleted_at IS NULL";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                orderStatusId = rs.getInt("id");
            } else {
                throw new RuntimeException("OrderStatus с именем '" + name + "' не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderStatusId;
    }

    // Вывод всех записей из таблицы
    @Override
    public List<OrderStatus> findAll() {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        String sql = "SELECT os.name as statusName FROM order_statuses WHERE deleted_at IS NULL";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderStatuses.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderStatuses;
    }

    // Удаление записи по id.
    // Удаление записи заменено на добавление отметки deleted_at, так как при удалении записи будут ошибки в запросе к таблице orders из-за потери ссылок.
    @Override
    public void deleteById(int id) {
        Connection conn = null;
        String sql = "UPDATE order_statuses os" +
                " SET deleted_at = NOW()" +
                " WHERE os.id = ?";
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Статус с id=" + id + " успешно удалён");
                } else {
                    System.out.println("Статус с id=" + id + " не найден");
                }
                conn.commit();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении товара: " + e.getMessage());
            try{
                conn.rollback(); // откат транзакции
                System.out.println("Транзакция откатилась.");
            } catch (SQLException rollbackEx) {
                System.err.println("Ошибка при rollback: " + rollbackEx.getMessage());
            }
        }
    }

    // Создание экземпляра класса из полученной строки
    private OrderStatus mapRow(ResultSet rs) throws SQLException {
        return new OrderStatus(
                rs.getInt("id"),
                rs.getString("statusName")
        );
    }
}
