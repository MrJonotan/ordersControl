package orderscontrol.model;

import orderscontrol.repository.Impl.OrderStatusRepositoryImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private int id;
    private int customerId;
    private String customerName;
    private int productId;
    private String productName;
    private final LocalDateTime orderDate;
    private int statusId;
    private String statusName;

    // Конструктор для создания/обновления записи в БД из кода
    public Order (Customer customer, Product product, LocalDateTime orderDate, String status){
        this.customerId = customer.getId();
        this.productId = product.getId();
        this.orderDate = orderDate;
        this.statusId = this.getStatusIdFromOS(status);
    }

    // Конструктор для создания экземпляра класса из запроса
    public Order (int id, String customerName, String productName, LocalDateTime orderDate, String statusName){
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.orderDate = orderDate;
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductName() {
        return productName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public int getCustomerId() { return customerId; }

    public int getProductId() { return productId; }

    public int getStatusId() { return statusId; }

    public void setId(int id) { this.id = id; }

    public void setCustomerId(Customer customer) {
        this.customerId = customer.getId();
    }

    public void setProductId(Product product) {
        this.productId = product.getId();
    }

    private int getStatusIdFromOS(String status){
        OrderStatusRepositoryImpl osri = new OrderStatusRepositoryImpl();
        return osri.findByName(status);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return "| " + customerName + " | " + productName + " | " + orderDate.format(formatter) + " | " + statusName + " |";
    }
}
