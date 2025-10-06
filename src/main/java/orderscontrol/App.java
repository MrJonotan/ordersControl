package orderscontrol;

import orderscontrol.model.Customer;
import orderscontrol.model.Order;
import orderscontrol.model.Product;
import orderscontrol.repository.Impl.CustomersRepositoryImpl;
import orderscontrol.repository.Impl.OrderRepositoryImpl;
import orderscontrol.repository.Impl.ProductRepositoryImpl;
import orderscontrol.util.ConfigLoader;
import org.flywaydb.core.Flyway;

import java.time.LocalDateTime;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Вызов метода миграции и заполнения таблиц
        createAndFillingDB();

        // Создание экземпляров работы с БД
        ProductRepositoryImpl pri = new ProductRepositoryImpl();
        CustomersRepositoryImpl cri = new CustomersRepositoryImpl();
        OrderRepositoryImpl ori = new OrderRepositoryImpl();

        // Создание экземпляра товара
        Product newProduct = new Product("Чайник", 1200.00F, 68, "Electronic");
        // Добавление записи в БД
        pri.create(newProduct);

        Customer newCustomer = new Customer("Никита Коновалов", "9867769087", "nikita@example.ru");
        cri.create(newCustomer);

        Order newOrder = new Order(newCustomer, newProduct, LocalDateTime.now(), "Новый");
        ori.create(newOrder);

        // Вывод последних 5 заказов
        List<Order> orders = ori.findLastFiveOrders();
        orders.stream().map(Order::toString).forEach(System.out::println);

        // Обновление цены и количества товара
        pri.updatePrice(newProduct, 1300.00F);
        pri.updateQuantity(newProduct, 4);

        // Удаление тестовых записей
        pri.deleteById(newProduct.getId());
        cri.deleteById(newCustomer.getId());
        ori.deleteById(newOrder.getId());
    }

    // Запускаем автоматический процесс миграции и заполнения созданных таблиц
    public static void createAndFillingDB() {
        Flyway flyway = Flyway.configure()
                .dataSource(ConfigLoader.get("jdbc.url"), ConfigLoader.get("jdbc.username"), ConfigLoader.get("jdbc.password"))
                .driver("org.postgresql.Driver")
                .schemas(ConfigLoader.get("jdbc.schema"))
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();
        System.out.println("Миграции успешно применены!");
    }
}