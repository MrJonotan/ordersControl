package orderscontrol.repository.Impl;

import orderscontrol.model.Customer;
import orderscontrol.repository.CustomerRepository;
import orderscontrol.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomersRepositoryImpl implements CustomerRepository {
    private final String selectQuery = "SELECT * FROM customers c";

    // Поиск по телефону
    @Override
    public List<Customer> findByPhone(String phone) {
        List<Customer> customers = new ArrayList<>();
        String sql = selectQuery + " WHERE c.phone = ? AND c.deleted_at IS NULL";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customers.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    // Поиск по email
    @Override
    public List<Customer> findByEmail(String email) {
        List<Customer> customers = new ArrayList<>();
        String sql = selectQuery + " WHERE c.phone = ? AND c.deleted_at IS NULL";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customers.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    // Создание записи
    @Override
    public Customer create(Customer entity) {
        Connection conn = null;
        String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";
        try {
            conn = DBConnection.getInstance().getConnection(); // установка соединения
            conn.setAutoCommit(false); // отключение автокоммита
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getPhone());
                ps.setString(3, entity.getEmail());

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
                System.out.println("Клиент: " + entity.getName() + " успешно добавлен");
                return entity;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении клиента: " + e.getMessage());
            try{
                conn.rollback(); // откат транзакции
                System.out.println("Транзакция откатилась.");
            } catch (SQLException rollbackEx) {
                System.err.println("Ошибка при rollback: " + rollbackEx.getMessage());
            }
        }
        return entity;
    }

    @Override
    public Optional<Customer> findById(int id) {
        Customer customer = null;
        String sql = selectQuery + " WHERE c.id = ? AND deleted_at IS NULL";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            customer = mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(customer);
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = selectQuery + " WHERE deleted_at IS NULL";
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

    // Удаление записи заменено на добавление отметки deleted_at, так как при удалении записи будут ошибки в запросе к таблице orders из-за потери ссылок.
    @Override
    public void deleteById(int id) {
        Connection conn = null;
        String sql = "UPDATE customers c" +
                " SET deleted_at = NOW()" +
                " WHERE c.id = ?";
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Клиент с id " + id + " успешно удалён");
                } else {
                    System.out.println("Клиент с id " + id + " не найден");
                }
                conn.commit();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении клиента: " + e.getMessage());
            try{
                conn.rollback(); // откат транзакции
                System.out.println("Транзакция откатилась.");
            } catch (SQLException rollbackEx) {
                System.err.println("Ошибка при rollback: " + rollbackEx.getMessage());
            }
        }
    }

    // Создание экземпляра класса из полученных из БД данных
    private Customer mapRow(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getString("email")
        );
    }
}
