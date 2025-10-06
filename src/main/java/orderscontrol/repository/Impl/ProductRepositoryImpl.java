package orderscontrol.repository.Impl;

import orderscontrol.model.Product;
import orderscontrol.repository.ProductRepository;
import orderscontrol.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// полностью готов
public class ProductRepositoryImpl implements ProductRepository {
    private final String selectQuery = "SELECT * FROM products p";

    // Создание записи
    @Override
    public void create(Product entity) {
        String sql = "INSERT INTO products (description, price, quantity, category) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        Product product = null;
        try {
            conn = DBConnection.getInstance().getConnection(); // установка соединения
            conn.setAutoCommit(false); // Отключение автокоммита
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getDescription());
                ps.setFloat(2, entity.getPrice());
                ps.setInt(3, entity.getQuantity());
                ps.setString(4, entity.getCategory());

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
                System.out.println("Товар: " + entity.getDescription() + " успешно добавлен");
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
    }

    // Поиск записи по id
    @Override
    public Optional<Product> findById(int id) {
        Product product = null;
        String sql = selectQuery + " WHERE p.id = ? AND deleted_at IS NULL";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            product = mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(product);
    }

    // Вывести все записи
    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
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

    // Удалить запись по id
    // Удаление записи заменено на добавление отметки deleted_at, так как при удалении записи будут ошибки в запросе к таблице orders из-за потери ссылок.
    @Override
    public void deleteById(int id) {
        Connection conn = null;
        String sql = "UPDATE products p" +
                " SET deleted_at = NOW()" +
                " WHERE p.id = ?";
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Товар с id " + id + " успешно удалён");
                } else {
                    System.out.println("Товар с id " + id + " не найден");
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

    // Поиск по продуктов по категории
    @Override
    public Product findByCategory(String category) {
        Product product = null;
        String sql = selectQuery + " WHERE p.category = ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            product = mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    // Обновление цены товара
    @Override
    public void updatePrice(Product product, float price) {
        Connection conn = null;
        String sql = "UPDATE products p" +
                " SET price = ?" +
                " WHERE p.id = ?";
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setFloat(1, price);
                ps.setInt(2, product.getId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Цена товара: " + product.getDescription() + " успешно обновлена.");
                } else {
                    System.out.println("Товар с id=" + product.getDescription() + " не найден");
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

    // Обновление количества товара
    @Override
    public void updateQuantity(Product product, int quantity) {
        Connection conn = null;
        String sql = "UPDATE products p" +
                " SET quantity = p.quantity + ?" +
                " WHERE p.id = ?";
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, quantity);
                ps.setInt(2, product.getId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Количество товара: " + product.getDescription() + " успешно обновлено.");
                } else {
                    System.out.println("Товар" + product.getDescription() + " не найден");
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
    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("id"),
            rs.getString("description"),
            rs.getFloat("price"),
            rs.getInt("quantity"),
            rs.getString("category")
        );
    }
}
